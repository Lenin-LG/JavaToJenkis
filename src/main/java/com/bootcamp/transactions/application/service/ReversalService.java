package com.bootcamp.transactions.application.service;

import com.bootcamp.transactions.application.service.dto.OperationResponse;
import com.bootcamp.transactions.domain.model.Operation;
import com.bootcamp.transactions.domain.model.OperationStatus;
import com.bootcamp.transactions.domain.model.OperationType;
import com.bootcamp.transactions.domain.port.in.ReversalUseCase;
import com.bootcamp.transactions.domain.port.out.AccountRepositoryPort;
import com.bootcamp.transactions.domain.port.out.OperationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReversalService implements ReversalUseCase {

    private final OperationRepositoryPort operationRepository;
    private final AccountRepositoryPort accountRepository;

    @Override
    @Transactional
    public Mono<OperationResponse> reverse(String operationId) {

        return operationRepository.findById(operationId)
                .switchIfEmpty(Mono.error(new RuntimeException("Operation not found")))
                .flatMap(originalOp -> {

                    // No permitir reversar una reversa
                    if (originalOp.getType() == OperationType.REVERSAL) {
                        return Mono.error(new RuntimeException("Cannot reverse a reversal"));
                    }

                    // No permitir doble reversa
                    if (originalOp.getStatus() == OperationStatus.REVERSED) {
                        return Mono.error(new RuntimeException("Operation already reversed"));
                    }

                    return accountRepository.findById(originalOp.getAccountId())
                            .switchIfEmpty(Mono.error(new RuntimeException("Account not found")))
                            .flatMap(account -> {

                                // Aplicar devolución
                                account.applyReversal(originalOp.getAmount());

                                // Crear operación reversal
                                Operation reversal = Operation.reversal(
                                        account.getAccountId(),
                                        originalOp.getAmount(),
                                        originalOp.getCurrency(),
                                        originalOp.getOperationId()
                                );

                                // Marcar operación original como REVERSED
                                originalOp.setStatus(OperationStatus.REVERSED);

                                return accountRepository.save(account)
                                        .then(operationRepository.save(originalOp))
                                        .then(operationRepository.save(reversal))
                                        .map(saved -> new OperationResponse(
                                                saved.getOperationId(),
                                                originalOp.getOperationId(),
                                                saved.getAccountId(),
                                                saved.getType().name(),
                                                saved.getAmount(),
                                                saved.getCurrency(),
                                                saved.getStatus().name(),
                                                account.getBalance(),
                                                saved.getCreatedAt()
                                        ));
                            });
                });
    }
}
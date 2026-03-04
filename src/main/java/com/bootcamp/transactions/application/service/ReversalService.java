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

    /*
        * Para reversar una operación, seguimos estos pasos:
        * 1. Buscar la operación original por su ID, si no existe lanzar excepción
        * 2. Verificar que la operación no sea una reversa, si es así lanzar excepción
        * 3. Verificar que la operación no haya sido ya reversada, si es así lanzar excepción
        * 4. Buscar la cuenta asociada a la operación original, si no existe lanzar excepción
        * 5. Aplicar la reversa a la cuenta (sumar el monto)
        * 6. Crear una nueva operación de tipo REVERSAL con estado COMPLETED y referencia a la operación original
        * 7. Marcar la operación original como REVERSED
        * 8. Guardar la cuenta actualizada, la operación original actualizada y la nueva operación de reversa en la base de datos
     */
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
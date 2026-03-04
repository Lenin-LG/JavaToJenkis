package com.bootcamp.transactions.application.service;

import com.bootcamp.transactions.application.service.dto.ChargeRequest;
import com.bootcamp.transactions.application.service.dto.OperationResponse;
import com.bootcamp.transactions.domain.exception.CurrencyMismatchException;
import com.bootcamp.transactions.domain.exception.NotFoundException;
import com.bootcamp.transactions.domain.model.Operation;
import com.bootcamp.transactions.domain.port.in.ChargeUseCase;
import com.bootcamp.transactions.domain.port.out.AccountRepositoryPort;
import com.bootcamp.transactions.domain.port.out.OperationRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargeService implements ChargeUseCase {

    private final AccountRepositoryPort accountRepository;
    private final OperationRepositoryPort operationRepository;
    private final TransactionalOperator txOperator;

    /*
        * Para aplicar un cargo, seguimos estos pasos:
        * 1. Buscar la cuenta por su ID, si no existe lanzar excepción
        * 2. Verificar que la moneda del cargo coincida con la moneda de la cuenta, si no lanzar excepción
        * 3. Aplicar el cargo a la cuenta (restar el monto)
        * 4. Crear una nueva operación de tipo CHARGE con estado COMPLETED
        * 5. Guardar la cuenta actualizada y la operación en la base de datos
     */
    @Override
    public Mono<OperationResponse> charge(ChargeRequest request) {

        return txOperator.execute(status ->
                accountRepository.findById(request.accountId())
                        .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
                        .flatMap(account -> {

                            log.info("Applying charge to account {}", account.getAccountId());

                            if (!account.getCurrency().equals(request.currency())) {
                                return Mono.error(new CurrencyMismatchException());
                            }

                            account.applyCharge(request.amount());

                            Operation operation = Operation.charge(
                                    account.getAccountId(),
                                    request.amount(),
                                    request.currency()
                            );

                            return accountRepository.save(account)
                                    .then(operationRepository.save(operation))
                                    .map(savedOp -> buildResponse(savedOp, account.getBalance()));
                        })
        ).single(); // importante
    }

    /*
        * Construir la respuesta a partir de la operación y el saldo actualizado de la cuenta
     */
    private OperationResponse buildResponse(Operation op, BigDecimal balanceAfter) {
        return new OperationResponse(
                op.getOperationId(),
                null,
                op.getAccountId(),
                op.getType().name(),
                op.getAmount(),
                op.getCurrency(),
                op.getStatus().name(),
                balanceAfter,
                op.getCreatedAt()
        );
    }
}
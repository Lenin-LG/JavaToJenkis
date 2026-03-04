package com.bootcamp.transactions.infrastructure.adapter.out.mysql;


import com.bootcamp.transactions.domain.model.Operation;
import com.bootcamp.transactions.domain.model.OperationStatus;
import com.bootcamp.transactions.domain.model.OperationType;
import com.bootcamp.transactions.domain.port.out.OperationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OperationRepositoryAdapter implements OperationRepositoryPort {

    private final SpringDataOperationRepository repository;

    /*
        * Guardar operación, si no existe crear nueva, si existe actualizar (optimistic locking)
     */
    @Override
    public Mono<Operation> save(Operation operation) {

        return repository.existsById(operation.getOperationId())
                .flatMap(exists -> {

                    OperationEntity entity = new OperationEntity();

                    entity.setOperationId(operation.getOperationId());
                    entity.setAccountId(operation.getAccountId());
                    entity.setType(operation.getType().name());
                    entity.setAmount(operation.getAmount());
                    entity.setCurrency(operation.getCurrency());
                    entity.setStatus(operation.getStatus().name());
                    entity.setReversedOperationId(operation.getReversedOperationId());
                    entity.setCreatedAt(operation.getCreatedAt());

                    if (!exists) {
                        entity.markNew(); //  SOLO si no existe
                    }

                    return repository.save(entity);
                })
                .map(this::toDomain);
    }

    @Override
    public Mono<Operation> findById(String operationId) {
        return repository.findById(operationId)
                .map(this::toDomain);
    }

    private Operation toDomain(OperationEntity entity) {
        return Operation.restore(
                entity.getOperationId(),
                entity.getAccountId(),
                OperationType.valueOf(entity.getType()),
                entity.getAmount(),
                entity.getCurrency(),
                OperationStatus.valueOf(entity.getStatus()),
                entity.getReversedOperationId(),
                entity.getCreatedAt()
        );
    }
}

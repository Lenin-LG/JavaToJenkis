package com.bootcamp.transactions.domain.port.out;

import com.bootcamp.transactions.domain.model.Operation;
import reactor.core.publisher.Mono;

public interface OperationRepositoryPort {

    Mono<Operation> save(Operation operation);

    Mono<Operation> findById(String operationId);
}

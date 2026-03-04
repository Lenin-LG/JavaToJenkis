package com.bootcamp.transactions.infrastructure.adapter.out.mysql;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SpringDataOperationRepository
        extends ReactiveCrudRepository<OperationEntity, String> {

    Mono<OperationEntity> findByOperationId(String operationId);

}

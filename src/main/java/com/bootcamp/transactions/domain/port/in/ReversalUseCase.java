package com.bootcamp.transactions.domain.port.in;

import com.bootcamp.transactions.application.service.dto.OperationResponse;
import reactor.core.publisher.Mono;

public interface ReversalUseCase {
    Mono<OperationResponse> reverse(String operationId);
}

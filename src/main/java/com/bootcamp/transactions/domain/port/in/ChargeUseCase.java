package com.bootcamp.transactions.domain.port.in;

import com.bootcamp.transactions.application.service.dto.ChargeRequest;
import com.bootcamp.transactions.application.service.dto.OperationResponse;
import reactor.core.publisher.Mono;

public interface ChargeUseCase {

    Mono<OperationResponse> charge(ChargeRequest request);
}

package com.bootcamp.transactions.domain.port.in;

import com.bootcamp.transactions.application.service.dto.BalanceResponse;
import reactor.core.publisher.Mono;

public interface GetBalanceUseCase {
    Mono<BalanceResponse> getBalance(String accountId);
}

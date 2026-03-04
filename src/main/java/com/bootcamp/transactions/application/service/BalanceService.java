package com.bootcamp.transactions.application.service;

import com.bootcamp.transactions.application.service.dto.BalanceResponse;
import com.bootcamp.transactions.domain.port.in.GetBalanceUseCase;
import com.bootcamp.transactions.domain.port.out.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BalanceService implements GetBalanceUseCase {

    private final AccountRepositoryPort accountRepository;

    @Override
    public Mono<BalanceResponse> getBalance(String accountId) {

        return accountRepository.findById(accountId)
                .map(acc -> new BalanceResponse(
                        acc.getAccountId(),
                        acc.getCurrency(),
                        acc.getBalance(),
                        acc.getUpdatedAt()
                ));
    }
}
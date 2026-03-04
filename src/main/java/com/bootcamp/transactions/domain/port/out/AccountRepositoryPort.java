package com.bootcamp.transactions.domain.port.out;

import com.bootcamp.transactions.domain.model.Account;
import reactor.core.publisher.Mono;

public interface AccountRepositoryPort {

    Mono<Account> findById(String accountId);

    Mono<Account> save(Account account);
}
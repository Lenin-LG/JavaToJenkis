package com.bootcamp.transactions.infrastructure.adapter.out.mysql;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SpringDataAccountRepository
        extends ReactiveCrudRepository<AccountEntity, String> {
}
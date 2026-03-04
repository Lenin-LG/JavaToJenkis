package com.bootcamp.transactions.infrastructure.adapter.out.mysql;

import com.bootcamp.transactions.domain.exception.NotFoundException;
import com.bootcamp.transactions.domain.model.Account;
import com.bootcamp.transactions.domain.port.out.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final SpringDataAccountRepository repository;

    /*
        * Buscar cuenta por ID, si no existe lanzar excepción
     */
    @Override
    public Mono<Account> findById(String accountId) {
        return repository.findById(accountId)
                .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
                .map(this::toDomain);
    }

    /*
        * Guardar cuenta, si no existe crear nueva, si existe actualizar (optimistic locking)
     */
    @Override
    public Mono<Account> save(Account account) {

        AccountEntity entity = new AccountEntity();

        entity.setAccountId(account.getAccountId());
        entity.setCurrency(account.getCurrency());
        entity.setBalance(account.getBalance());
        entity.setUpdatedAt(account.getUpdatedAt());
        entity.setVersion(account.getVersion());

        return repository.save(entity)
                .map(this::toDomain);
    }
    /*
        * Convertir entidad a dominio
     */
    private Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getAccountId(),
                entity.getCurrency(),
                entity.getBalance(),
                entity.getUpdatedAt(),
                entity.getVersion() //  importante
        );
    }
}
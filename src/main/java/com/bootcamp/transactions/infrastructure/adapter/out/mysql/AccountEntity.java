package com.bootcamp.transactions.infrastructure.adapter.out.mysql;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("accounts")
public class AccountEntity {

    @Id
    @Column("account_id")
    private String accountId;
    private String currency;
    private BigDecimal balance;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Version
    private Long version;
}

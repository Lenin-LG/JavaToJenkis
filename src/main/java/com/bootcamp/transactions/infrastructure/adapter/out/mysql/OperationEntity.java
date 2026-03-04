package com.bootcamp.transactions.infrastructure.adapter.out.mysql;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("operations")
public class OperationEntity implements Persistable<String> {

    @Id
    @Column("operation_id")
    private String operationId;

    @Column("account_id")
    private String accountId;

    private String type;
    private BigDecimal amount;
    private String currency;
    private String status;

    @Column("reversed_operation_id")
    private String reversedOperationId;

    @Column("created_at")
    private LocalDateTime createdAt;

    //  control real de nuevo o existente
    @Transient
    private boolean isNew;

    @Override
    public String getId() {
        return operationId;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    public void markNew() {
        this.isNew = true;
    }
}
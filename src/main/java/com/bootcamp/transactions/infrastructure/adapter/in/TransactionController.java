package com.bootcamp.transactions.infrastructure.adapter.in;

import com.bootcamp.transactions.application.service.dto.BalanceResponse;
import com.bootcamp.transactions.application.service.dto.ChargeRequest;
import com.bootcamp.transactions.application.service.dto.OperationResponse;
import com.bootcamp.transactions.domain.port.in.ChargeUseCase;
import com.bootcamp.transactions.domain.port.in.GetBalanceUseCase;
import com.bootcamp.transactions.domain.port.in.ReversalUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {

    private final ChargeUseCase chargeUseCase;
    private final GetBalanceUseCase balanceUseCase;
    private final ReversalUseCase reversalUseCase;
    /**
     * Cargo a una cuenta
     */
    @PostMapping("/transactions/charge")
    public Mono<ResponseEntity<OperationResponse>> charge(@RequestBody ChargeRequest request) {
        return chargeUseCase.charge(request)
                .map(res -> ResponseEntity.status(201).body(res));
    }
    /**
     *  Consultar saldo
     */
    @GetMapping("/accounts/{accountId}/balance")
    public Mono<ResponseEntity<BalanceResponse>> balance(@PathVariable String accountId) {
        return balanceUseCase.getBalance(accountId)
                .map(ResponseEntity::ok);
    }
    /**
     * Reversar operación
     */
    @PostMapping("/transactions/{operationId}/reversal")
    public Mono<ResponseEntity<OperationResponse>> reversal(@PathVariable String operationId) {
        return reversalUseCase.reverse(operationId)
                .map(res -> ResponseEntity.status(201).body(res));
    }
}

package com.wallet.transaction_service.Controller;

import com.wallet.transaction_service.DTO.TransactionRequest;
import com.wallet.transaction_service.DTO.TransactionResponse;
import com.wallet.transaction_service.Repository.TransactionRepository;
import com.wallet.transaction_service.Service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest transactionRequest, @RequestHeader("Idempotency-Key") String idempotencyKey){
        return ResponseEntity.accepted().body(transactionService.createTransfer(transactionRequest, idempotencyKey));
    }

    @GetMapping("/{Id}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable Long Id){
        return ResponseEntity.ok(transactionService.getTransfer(Id));
    }
}

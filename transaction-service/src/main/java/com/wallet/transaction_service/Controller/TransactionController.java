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

    // Controller
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestBody TransactionRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            jakarta.servlet.http.HttpServletRequest httpRequest) {

        Long authenticatedUserId = (Long) httpRequest.getAttribute("userId");
        TransactionResponse response = transactionService.createTransfer(request, idempotencyKey, authenticatedUserId);
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable Long Id){
        return ResponseEntity.ok(transactionService.getTransfer(Id));
    }
}

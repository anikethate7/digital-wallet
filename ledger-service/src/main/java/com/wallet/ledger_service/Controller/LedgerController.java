package com.wallet.ledger_service.Controller;

import com.wallet.ledger_service.DTO.LedgerEntryResponse;
import com.wallet.ledger_service.Service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<LedgerEntryResponse>> getEntriesForTransaction(@PathVariable Long transactionId) {
        return ResponseEntity.ok(ledgerService.getEntriesForTransaction(transactionId));
    }
}
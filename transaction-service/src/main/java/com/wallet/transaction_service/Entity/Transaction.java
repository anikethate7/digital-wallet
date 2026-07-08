package com.wallet.transaction_service.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String idempotencyKey;

    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdDate;
}

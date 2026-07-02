package com.wallet.account_service.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    @Column(unique = true)
    private String accountNumber;
    private String accountType;   // SAVINGS, CURRENT, WALLET
    private BigDecimal balance;
    private String currency;
    private String status;        // ACTIVE, FROZEN, CLOSED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
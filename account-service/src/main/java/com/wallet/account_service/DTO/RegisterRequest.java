package com.wallet.account_service.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
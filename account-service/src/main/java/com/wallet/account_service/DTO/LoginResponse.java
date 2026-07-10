package com.wallet.account_service.DTO;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Long userId;
    private String username;
}
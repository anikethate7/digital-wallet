package com.wallet.transaction_service.Client;

import com.wallet.transaction_service.Config.FeignClientConfig;
import com.wallet.transaction_service.DTO.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "account-service", configuration = FeignClientConfig.class)
public interface AccountClient {

    @GetMapping("/api/accounts/{id}/balance")
    BigDecimal getBalance(@PathVariable("id") Long id);

    @GetMapping("/api/accounts/{id}")
    AccountResponse getAccountDetails(@PathVariable("id") Long id);
}
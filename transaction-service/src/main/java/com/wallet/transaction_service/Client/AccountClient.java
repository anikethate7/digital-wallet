package com.wallet.transaction_service.Client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("/api/accounts/{id}/balance")
    BigDecimal getBalance(@PathVariable("id") Long id);
}

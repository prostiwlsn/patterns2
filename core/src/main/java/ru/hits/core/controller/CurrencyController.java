package ru.hits.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.core.domain.dto.currency.CurrencyEnum;
import ru.hits.core.service.CurrencyService;

import java.util.Map;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/rates")
    public Map<String, Double> getRates(
            @RequestParam CurrencyEnum currency
    ) {
        return currencyService.getExchangeRates(currency);
    }
}
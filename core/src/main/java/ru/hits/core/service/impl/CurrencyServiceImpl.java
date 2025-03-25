package ru.hits.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hits.core.domain.dto.currency.CurrencyEnum;
import ru.hits.core.domain.dto.currency.CurrencyResponse;
import ru.hits.core.service.CurrencyService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    private final RestTemplate restTemplate;

    public Map<String, Double> getExchangeRates(CurrencyEnum from) {
        CurrencyResponse response = restTemplate.getForObject(API_URL + from.name(), CurrencyResponse.class);
        return response != null ? response.getRates() : null;
    }

    public Float getCurrencyValue(CurrencyEnum from, CurrencyEnum to) {
        CurrencyResponse response = restTemplate.getForObject(API_URL + from.name(), CurrencyResponse.class);
        return response != null ? response.getRates().get(to.name()).floatValue() : null;
    }
}
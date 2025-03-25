package ru.hits.core.service;

import ru.hits.core.domain.dto.currency.CurrencyEnum;

import java.util.Map;

public interface CurrencyService {

    Map<String, Double> getExchangeRates(CurrencyEnum from);

    Float getCurrencyValue(CurrencyEnum from, CurrencyEnum to);

}

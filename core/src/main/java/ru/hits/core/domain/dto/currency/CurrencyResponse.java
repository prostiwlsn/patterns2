package ru.hits.core.domain.dto.currency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyResponse {

    @JsonProperty("base")
    private String baseCurrency;

    @JsonProperty("date")
    private String date;

    @JsonProperty("rates")
    private Map<String, Double> rates;

}
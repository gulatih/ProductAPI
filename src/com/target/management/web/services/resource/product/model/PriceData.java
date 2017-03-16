package com.target.management.web.services.resource.product.model;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class PriceData {

	private final BigDecimal value;
	@JsonProperty("currency_code")
	private final String currencyCode;

	/**
	 * 
	 * @param value
	 * @param currencyCode
	 */
	@JsonCreator
	public PriceData(@JsonProperty("value") BigDecimal value, @JsonProperty("currency_code") String currencyCode)
	{
		this.value = value;
		this.currencyCode = currencyCode;
	}
	
	/**
	 * 
	 * @return value
	 */
	public BigDecimal getValue()
	{
		return value;
	}
	
	/**
	 * 
	 * @return currency code
	 */
	public String getCurrencyCode()
	{
		return currencyCode;
	}
}

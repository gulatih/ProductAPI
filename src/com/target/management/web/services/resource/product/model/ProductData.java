package com.target.management.web.services.resource.product.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents Product Data
 * @author HH026169
 *
 */
public class ProductData {

	private final long id;
	private final String name;
	@JsonProperty("current_price")
	private final PriceData currentPrice;
	
	
	/**
	 * @param id
	 * @param name
	 * @param currentPrice
	 */
	@JsonCreator
	public ProductData(@JsonProperty("id") long id, @JsonProperty("name") String name, @JsonProperty("current_price") PriceData currentPrice)
	{
	  	this.id = id;
	  	this.name = name;
	  	this.currentPrice = currentPrice;
	}
	
	
	/**
	 * 
	 * @return Product ID
	 */
	public long getID()
	{
		return id;
	}
	
	/**
	 * 
	 * @return Product Name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 
	 * @return Product Price
	 */
	public PriceData getCurrentPrice()
	{
		return currentPrice;
	}
	
}


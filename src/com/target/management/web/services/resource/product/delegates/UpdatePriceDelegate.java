package com.target.management.web.services.resource.product.delegates;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.target.management.web.services.data.CassandraDelegate;
import com.target.management.web.services.resource.product.model.ProductData;
import com.target.management.web.services.utils.CQLUtils;

public class UpdatePriceDelegate implements CassandraDelegate<List<ProductData>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePriceDelegate.class);
	private static String cql;
	private long id;
	private BigDecimal price;
	private String currencyCode;

	static {
		try {
			cql = CQLUtils.loadCQL(UpdatePriceDelegate.class.getResourceAsStream("cql/updatepricebyid.cql"));
		} catch (IOException e) {
			LOGGER.error("Could not load base CQL", e);
		}
	}

	public UpdatePriceDelegate(long id, BigDecimal price, String currencyCode) {
		this.id = id;
		this.price = price;
		this.currencyCode = currencyCode;
	}

	public String getCQL() {
		return cql;
	}

	public BoundStatement prepare(PreparedStatement statement) {
		BoundStatement bound = statement.bind(price, currencyCode, id);
		return bound;
	}

	public List<ProductData> translate(ResultSet results) {
		//do nothing
		return null;
	}

}

package com.target.management.web.services.resource.product.delegates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.target.management.web.services.data.CassandraDelegate;
import com.target.management.web.services.resource.product.model.PriceData;
import com.target.management.web.services.utils.CQLUtils;

public class PriceDelegate implements CassandraDelegate<List<PriceData>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(PriceDelegate.class);
	private static String cql;
	private long id;

	 static
	   {
	      try
	      {
	         cql = CQLUtils.loadCQL(PriceDelegate.class.getResourceAsStream("cql/pricebyid.cql"));
	      }
	      catch (IOException e)
	      {
	         LOGGER.error("Could not load base CQL", e);
	      }
	   }

	public PriceDelegate(long id) {
		this.id = id;
	}

	public String getCQL() {
		return cql;
	}

	public BoundStatement prepare(PreparedStatement statement) {
		BoundStatement bound = statement.bind(id);
		return bound;
	}

	public List<PriceData> translate(ResultSet results) {
		List<PriceData> products = new ArrayList<PriceData>();
		while(!results.isExhausted())
		{
			Row row = results.one();
			products.add(new PriceData(row.getDecimal(0), row.getString(1)));
		}
		return products;
	}

}

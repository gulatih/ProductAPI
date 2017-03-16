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
import com.target.management.web.services.resource.product.model.ProductData;
import com.target.management.web.services.utils.CQLUtils;

public class AllProductDelegate implements CassandraDelegate<List<ProductData>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AllProductDelegate.class);
	private static String cql;

	 static
	   {
	      try
	      {
	         cql = CQLUtils.loadCQL(AllProductDelegate.class.getResourceAsStream("cql/listproducts.cql"));
	      }
	      catch (IOException e)
	      {
	         LOGGER.error("Could not load base CQL", e);
	      }
	   }
	 
	public String getCQL() {
		return cql;
	}

	public BoundStatement prepare(PreparedStatement statement) {
		BoundStatement bound = statement.bind();
		//nothing to set here
		return bound;
	}

	public List<ProductData> translate(ResultSet results) {
		List<ProductData> products = new ArrayList<ProductData>();
		while(!results.isExhausted())
		{
			Row row = results.one();
			products.add(new ProductData(row.getLong(0),row.getString(1), new PriceData(row.getDecimal(2), row.getString(3))));
		}
		return products;
	}

}

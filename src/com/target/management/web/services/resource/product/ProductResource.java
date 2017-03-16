package com.target.management.web.services.resource.product;

import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qmino.miredot.annotations.ReturnType;
import com.sun.jersey.api.client.ClientResponse;
import com.target.management.web.services.data.CassandraDelegateExecutor;
import com.target.management.web.services.data.RestClient;
import com.target.management.web.services.resource.product.delegates.AllProductDelegate;
import com.target.management.web.services.resource.product.delegates.PriceDelegate;
import com.target.management.web.services.resource.product.delegates.ProductDelegate;
import com.target.management.web.services.resource.product.delegates.UpdatePriceDelegate;
import com.target.management.web.services.resource.product.model.PriceData;
import com.target.management.web.services.resource.product.model.ProductData;

@Path("/product")
public class ProductResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);
	private static final String PRODUCT = "product";
	private static final String PRODUCT_DESCRIPTION = "product_description";
	private static final String ITEM = "item";
	private static final String TITLE = "title";

	@Path("/all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ReturnType("java.util.List<com.target.management.web.services.resource.product.model.ProductData>")
	public Response getAllProducts() {
		// authorize();
		try {
			Object results = CassandraDelegateExecutor.execute(new AllProductDelegate());
			return Response.ok(results, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			LOGGER.error("Could not retrieve product list.", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}

	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProducts(@PathParam("id") long id) {
		try {
			Object results = CassandraDelegateExecutor.execute(new ProductDelegate(id));
			return Response.ok(results, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			LOGGER.error("Could not retrieve product based on id.", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}

	@Path("/name")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveProductName() {
		ClientResponse response = RestClient.getInstance().connect(
				"http://redsky.target.com/v1/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics");

		if (response.getStatus() != 200) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		Object title = new JSONObject(response.getEntity(String.class)).getJSONObject(PRODUCT).getJSONObject(ITEM)
				.getJSONObject(PRODUCT_DESCRIPTION).get(TITLE);

		return Response.ok(title, MediaType.TEXT_PLAIN).build();
	}

	@Path("/{id}/{name}/price")
	@GET
	public Response retrievePrice(@PathParam("id") long id, @PathParam("name") String name) {
		try {
			List<PriceData> results = (List<PriceData>) CassandraDelegateExecutor.execute(new PriceDelegate(id));
			List<ProductData> products = new ArrayList<ProductData>();
			for (PriceData priceData : results) {
				products.add(new ProductData(id, name, priceData));
			}
			return Response.ok(products, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			LOGGER.error("Could not retrieve price.", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}

	@Path("/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePrice(@PathParam("id") long id, ProductData product) {
		try {
			new CassandraDelegateExecutor().execute(new UpdatePriceDelegate(id, product.getCurrentPrice().getValue(),
					product.getCurrentPrice().getCurrencyCode()));
			return Response.ok().build();
		} catch (Exception e) {
			LOGGER.error("Could not update price.", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}
}

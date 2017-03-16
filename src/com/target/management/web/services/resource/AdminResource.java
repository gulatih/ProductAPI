package com.target.management.web.services.resource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.target.management.web.services.data.CassandraConnectionManager;

@Path("/")
public class AdminResource {

	private static final Logger logger = LoggerFactory.getLogger(AdminResource.class);

	/**
	 * Landing Page
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getLandingPage() {
		return buildPage("Target Rest Webservice",
				"<STYLE type=\"text/css\"body{font-family: arial;font-size:12px; }></STYLE>", "Target Rest Service");
	}

	/**
	 * 
	 * @param title
	 * @param style
	 * @param body
	 * @return HTML String
	 */
	private String buildPage(String title, String style, String body) {
		StringBuffer buf = new StringBuffer();
		// open the page
		buf.append("<HTML>");

		// set the header information
		buf.append("<HEAD>");
		buf.append("<title>" + title + "</title>");
		if (style != null) {
			buf.append(style);
		}
		buf.append("</HEAD>");

		// now set the page context
		buf.append("<BODY>");
		buf.append(body);
		buf.append("</BODY>");

		// close the page
		buf.append("</HTML>");
		return buf.toString();

	}

	/**
	 * Should be used by monitoring solutions to check the health of the
	 * service.
	 * 
	 * @return response code - HTTP 200 is good, everything else is bad
	 */
	@Path("/health/status")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getHealthResponse() {
		Session session = null;
		try {
			CassandraConnectionManager.getInstance().connect();
			CassandraConnectionManager.getInstance().getCluster();
			session = CassandraConnectionManager.getInstance().getSession();
		} catch (Throwable e) {
			logger.info("Could not connect to the database.", e);
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
		} finally {
			CassandraConnectionManager.safeCloseSession(session);
		}

		return Response.status(Response.Status.OK).build();
	}

	/**
	 * Human friendly page that displays health status information
	 * 
	 * @return health status page
	 */
	@Path("/health")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getHealth() {
		boolean isConnected = true;
		Metadata metadata = null;
		Session session = null;
		try {
			metadata = CassandraConnectionManager.getInstance().getCluster().getMetadata();
			session = CassandraConnectionManager.getInstance().getSession();
		} catch (Throwable e) {
			logger.info("Could not connect to the database.", e);
			isConnected = false;
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
		} finally {
			CassandraConnectionManager.safeCloseSession(session);
		}

		StringBuffer buf = new StringBuffer("<h2>Target Restful Webservice </h2><br>");

		buf.append("<b>Service Check</b><br>");
		buf.append("Database available - ").append(isConnected);
		buf.append("<br>");
		buf.append("Cluster Name - ").append(metadata.getClusterName());

		return buildPage("Target Rest Service",
				"<STYLE type=\"text/css\"body{font-family: arial;font-size:12px; }></STYLE>", buf.toString());
	}

}

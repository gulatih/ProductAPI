package com.target.management.web.services.data;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class CassandraConnectionManager {

	private static CassandraConnectionManager instance;
	private InitialContext ic = null;
	private Session session;
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraConnectionManager.class);
	private static final String CASSANDRA_NODE = "config/cassandra/node";
	private static final String CASSANDRA_PORT = "config/cassandra/port";
	private static String node;
	private static Integer port;

	/** Cassandra Cluster. */
	private static Cluster cluster;
	

	private CassandraConnectionManager() throws NamingException {
		ic = new InitialContext();
		Object envCtx = ic.lookup("java:comp/env");
		node = (String) ((javax.naming.Context) envCtx).lookup(CASSANDRA_NODE);
		port = (Integer) ((javax.naming.Context) envCtx).lookup(CASSANDRA_PORT);

	}

	/**
	 * 
	 * @return instance of class
	 */
	public static CassandraConnectionManager getInstance() {
		if (instance == null) {
			synchronized (CassandraConnectionManager.class) {
				if (instance == null) {
					try {
						instance = new CassandraConnectionManager();
						//create cluster only once for the lifetime of application
						cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
					} catch (NamingException e) {
						LOGGER.error("Could not instantiate Connection Manager.", e);
					}
				}
			}
		}
		return instance;
	}

	/**
	 * Gets a connection to the default data store.
	 * 
	 * @return connection
	 * @throws NoHostAvailableException
	 */
	public void connect() throws NoHostAvailableException {
		session = cluster.connect();
	}

	/**
	 * 
	 * @return session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * 
	 * @return cluster
	 */
	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * Safely close the session without throwing an exception.
	 * 
	 * @param session
	 */
	public static void safeCloseSession(Session session) {
		if (session != null) {
			try {
				session.close();
			} catch (Exception e) {
				LOGGER.error("Error closing session", e);
			}
		}
	}

	/**
	 * Safely close the cluster without throwing an exception.
	 * 
	 * @param cluster
	 */
	public static void safeCloseCluster(Cluster cluster) {
		if (cluster != null) {
			try {
				cluster.close();
			} catch (Exception e) {
				LOGGER.error("Error closing connection", e);
			}
		}
	}
}

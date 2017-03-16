package com.target.management.web.services.data;

import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class CassandraDelegateExecutor {

	private static Session session;
	private static PreparedStatementCache psCache = new CassandraDelegateExecutor().new PreparedStatementCache();

	/**
	 * 
	 * @param DAO
	 * @return resulting objects from the DAO
	 */
	static public Object execute(final CassandraDelegate<?> DAO) {
		ResultSet results = null;

		try {
			CassandraConnectionManager.getInstance().connect();
			// Get session
			session = CassandraConnectionManager.getInstance().getSession();

			// Prepare the statement
			BoundStatement bound = DAO.prepare(psCache.getStatement(DAO.getCQL()));

			// Execute and get the result set
			results = session.execute(bound);

			// Let the DAO translate the results
			return DAO.translate(results);
		} finally {
			try {
				CassandraConnectionManager.safeCloseSession(session);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Cache the Prepared Statement so that we create it only once
	 * 
	 * @author HH026169
	 *
	 */
	private class PreparedStatementCache {
		Map<String, PreparedStatement> statementCache = new HashMap<>();

		public PreparedStatement getStatement(String cql) {
			PreparedStatement ps = statementCache.get(cql);
			// statement isn't cached, create one and cache it now.
			if (ps == null) {
				ps = session.prepare(cql);
				statementCache.put(cql, ps);
			}
			return ps;
		}
	}
}

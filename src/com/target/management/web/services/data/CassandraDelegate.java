package com.target.management.web.services.data;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;

public interface CassandraDelegate<t> {

	/**
	 * @return CQL statement to be prepared
	 */
	public String getCQL();

	/**
	 * Implementors should set any parameters on the prepared statement in this
	 * method.
	 * 
	 * @param statement
	 */
	public BoundStatement prepare(PreparedStatement statement);

	/**
	 * Implementors can translate the CQL resultset to any desired Object for easier consumption
	 * @param results
	 * @return resulting objects
	 */
	public t translate(ResultSet results);

}

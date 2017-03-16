package com.target.management.web.services.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CQLUtils {

	/**
	 * Loads CQL file
	 * 
	 * @param cqlFileStream
	 * @return String representation of the CQL file
	 * @throws IOException
	 */
	public static String loadCQL(InputStream cqlFileStream) throws IOException {
		BufferedReader cqlFileReader = new BufferedReader(new InputStreamReader(cqlFileStream));
		StringBuffer cqlBuffer = new StringBuffer();

		String cqlLine;
		while ((cqlLine = cqlFileReader.readLine()) != null) {
			cqlBuffer.append(cqlLine);
			cqlBuffer.append(' ');
		}

		return cqlBuffer.toString();
	}

}

package org.communinet.billing.impl.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDao {
	
	private static final String CONFIG_FILENAME = "config/datasource.properties";
	private Properties properties;
	private Connection connection;

	static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);
	
	public AbstractDao() {
		try {
			properties = loadProperties(new File(CONFIG_FILENAME));
			connection = DriverManager.getConnection(properties.getProperty("javax.persistence.jdbc.url"), properties);
		} catch (IOException | SQLException e) {
			logger.debug("Unable to initialise datasource");
			throw new RuntimeException(e);

		}
		
	}
	/*
	 * 	javax.persistence.jdbc.user=dbuser
	 *	javax.persistence.jdbc.driver=org.apache.derby.jdbc.ClientDriver
	 *	javax.persistence.jdbc.url=jdbc:derby://localhost:49153/cNetBilling;create=true
	 */
	protected Connection getDatabaseConnection(){
		return connection;
	}

	protected Properties loadProperties(File f) throws FileNotFoundException,
			IOException {
		Properties pro = new Properties();

		if (f.exists()) {

			FileInputStream in = new FileInputStream(f);
			pro.load(in);
		}
		return pro;
	}
}

package org.communinet.billing.impl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

import org.communinet.billing.domain.IPTraffic;
import org.communinet.billing.impl.IDataAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DataDao extends AbstractDao implements IDataAdapter {

	static final Logger logger = LoggerFactory.getLogger(DataDao.class);
	
	public DataDao() throws Exception
	{
		logger.info("Created instance of {}", this.getClass().getName());
	}
	
	
	public void addDataUsage(IPTraffic ip_data, double usage) throws Exception {
		
		
		try(Connection connection = getDatabaseConnection();){
			String sql = "insert into datausage (IP_ADDRESS,BYTES,DATE,PORT,UPLOAD,PROTOCOL)"
					+ "values (?,?,?,?,?,?)";
			
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			
			int parameterIndex = 1;
			prepareStatement.setString(parameterIndex++, ip_data.getIp());
			prepareStatement.setDouble(parameterIndex++, usage);
			prepareStatement.setTimestamp(parameterIndex++, new Timestamp(new Date().getTime()));
			prepareStatement.setInt(parameterIndex++, Integer.parseInt(ip_data.getPort()));
			prepareStatement.setInt(parameterIndex++, ip_data.isSource() ? 1 :0);
			prepareStatement.setString(parameterIndex++, ip_data.getProtocol());
			
			prepareStatement.executeUpdate();
			
		}
		
	}
	
	

}

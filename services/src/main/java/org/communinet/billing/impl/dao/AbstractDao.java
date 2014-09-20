package org.communinet.billing.impl.dao;

import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDao {
	private static Map<String, EntityManager> entityManagerMap = new Hashtable<String, EntityManager>();
	private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

	private static final String configFilename = "config/datasource.properties";
	private static Properties configProperties;
	
	public AbstractDao() {
		
		try {
			configProperties = new Properties();
			configProperties.load(new FileReader(configFilename));
		}catch(IOException io){
			logger.error("Unable to instantiate DAO. Failed with {}", io);
			throw new RuntimeException(io);
		}
		
	}
	
	
	public EntityManager getEntityManager(String peristenceUnit){
		EntityManager entityManager = entityManagerMap.get(peristenceUnit);
		
		if(entityManager == null){
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(peristenceUnit,configProperties);
			entityManager = emf.createEntityManager();
			entityManagerMap.put(peristenceUnit, entityManager);
		}
		
		return entityManager;
	}
	
	protected void persistData(Object data, EntityManager entityManager) {
		entityManager.getTransaction().begin();
		entityManager.persist(data);
		entityManager.getTransaction().commit();
		
	}

}

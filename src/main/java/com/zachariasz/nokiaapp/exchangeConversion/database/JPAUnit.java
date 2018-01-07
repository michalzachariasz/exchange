package com.zachariasz.nokiaapp.exchangeConversion.database;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUnit {
	private static final String PERSISTENCE_UNIT_NAME = "database";
	  private static EntityManagerFactory entityManagerFactory;
	  
	  
	  public static EntityManagerFactory getEntityManagerFactory() {
	    if (entityManagerFactory == null) {
	      entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    }
	    return entityManagerFactory;
	  }

	  public static void shutdownEntityManagerFactory() {
	    if (entityManagerFactory != null) {
	      entityManagerFactory.close();
	    }
	  }
}

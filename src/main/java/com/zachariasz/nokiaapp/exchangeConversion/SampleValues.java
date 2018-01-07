package com.zachariasz.nokiaapp.exchangeConversion;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.zachariasz.nokiaapp.exchangeConversion.database.JPAUnit;
import com.zachariasz.nokiaapp.exchangeConversion.model.Exchange;

public class SampleValues {
	public static void addSampleValuesToDatabase() {
		EntityManager entityManager = JPAUnit.getEntityManagerFactory().createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		
		try {
			transaction = entityManager.getTransaction();
			transaction.begin();
			entityManager.persist(new Exchange("EUR","4.1763"));
			entityManager.persist(new Exchange("USD","3.4771"));
			entityManager.persist(new Exchange("GBP","4.6933"));
			entityManager.persist(new Exchange("CHF","3.5682"));
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
		} finally {
			entityManager.close();
			JPAUnit.shutdownEntityManagerFactory();
		}
	}

}

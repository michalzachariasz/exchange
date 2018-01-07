package com.zachariasz.nokiaapp.exchangeConversion.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.zachariasz.nokiaapp.exchangeConversion.database.JPAUnit;
import com.zachariasz.nokiaapp.exchangeConversion.model.Exchange;


public class ExchangeServiceWithoutGet implements ExchangeServiceWihoutGetInterface{
	//It should be open when session is starting and close when session is closing
	private EntityManager entityManager = JPAUnit.getEntityManagerFactory().createEntityManager();
	private EntityTransaction transaction = null;

	public String addExchange(Exchange exchange) {
		if (!databaseContainExchangeRate(exchange.getId())) {
			try {
				transaction = entityManager.getTransaction();
				transaction.begin();
				entityManager.persist(exchange);
				transaction.commit();
				return "Added succesfully";
			} catch (Exception e) {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
				}
				return e.getMessage();
			} finally {
				entityManager.close();
			}
		}
		return "This exchange rate alredy exist";
	}

	public String deleteExchange(String exchangeRateName) {
		if(databaseContainExchangeRate(exchangeRateName)) {
			try {
				transaction = entityManager.getTransaction();
				transaction.begin();
				entityManager.remove(entityManager.find(Exchange.class, exchangeRateName));
				transaction.commit();
				return "Removed succesfully";
			} catch (Exception e) {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
				}
				return e.getMessage();
			} finally {
				entityManager.close();
			}
		}
		return "This exchange rate does not exist in database";
	}

	private boolean databaseContainExchangeRate(String exchangeRate) {
		try {
			TypedQuery<Exchange> query = entityManager.createQuery("select e from Exchange e where e.id = :exchangeId", Exchange.class);
			query.setParameter("exchangeId", exchangeRate);
			query.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}
}

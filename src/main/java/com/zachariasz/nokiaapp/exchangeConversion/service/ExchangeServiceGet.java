package com.zachariasz.nokiaapp.exchangeConversion.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.zachariasz.nokiaapp.exchangeConversion.database.JPAUnit;
import com.zachariasz.nokiaapp.exchangeConversion.model.Exchange;




public class ExchangeServiceGet implements ExchangeServiceGetInterface {
	//Entity manager factory should be open when session is starting and close when session is closing
	private EntityManager entityManager = null;
	private String baseExchange = "PLN";
	private final String errorDatabaseMessage = "One or more parameters does not exist in database";
	private final String databaseExceptionMessage = "Check database settings";
	private final String noResultMessage = "Database does not contain this value";
	 
	
	public List<Exchange> getAllExchanges() {
		entityManager = JPAUnit.getEntityManagerFactory().createEntityManager();
		try {	
		TypedQuery<Exchange> query = entityManager.createQuery("select e from Exchange e", Exchange.class);
		List<Exchange> resultList = query.getResultList();
		return resultList;
		}catch(Exception e) {
			return createErrorList();
		}finally {
			entityManager.close();
		}
	}

	public String getExchange(List<String> parameters) {
		if(bothParametersAreEqual(parameters)) {
			return "1.0";
		}
		else if(bothParametersAreDifferentFromBaseExchange(parameters)) {
			String exchangeRateValue1 = getExchangeRatesFromDatabase(parameters.get(0));
			String exchangeRateValue2 = getExchangeRatesFromDatabase(parameters.get(1));
			if(exchangeRateValue1 != noResultMessage && exchangeRateValue2 != noResultMessage) {
				return String.valueOf(calculateDesiredExchangeRate(exchangeRateValue1, exchangeRateValue2));
			}
			return errorDatabaseMessage;
		}
		else if(secondParameterIsDifferentFromBaseExchange(parameters)) {
			String exchangeRateValue = getExchangeRatesFromDatabase(parameters.get(1));
			if(exchangeRateValue != noResultMessage )
				return String.valueOf(calculateDesiredExchangeRate("1.0",exchangeRateValue));
			return errorDatabaseMessage;
		}
		else if(firstParameterIsDifferentFromBaseExchange(parameters)) {
			String exchangeRateValue = getExchangeRatesFromDatabase(parameters.get(0));
			if(exchangeRateValue != noResultMessage)
				return String.valueOf(calculateDesiredExchangeRate(exchangeRateValue ,"1.0"));
			return errorDatabaseMessage;
		}
		return errorDatabaseMessage;
	}
				
	
	private List<Exchange> createErrorList() {
		Exchange exchange = new Exchange("error", "0.0");
		List<Exchange> resultlist = new ArrayList<>();
		resultlist.add(exchange);
		return resultlist;
	}


	private boolean exchangeRateIsEqualToBaseExchange(String exchangeRate) {
		if(exchangeRate.toUpperCase().equals(baseExchange))
			return true;
		return false;
	}

	private double calculateDesiredExchangeRate(String firstValue, String secondValue) {
		return parseExchangeRateToDouble(firstValue) / parseExchangeRateToDouble(secondValue);
	}

	private String getExchangeRatesFromDatabase(String queryParameter) {
		entityManager = JPAUnit.getEntityManagerFactory().createEntityManager();
		try {
		Query query = entityManager.createQuery("select e.value from Exchange e where e.id = :exchangeId");
		query.setParameter("exchangeId", queryParameter);
		return (String) query.getSingleResult();
		}catch(NoResultException e) {
			return noResultMessage;
		}
		catch(Exception e) {
			return databaseExceptionMessage;
		}finally {
			entityManager.close();
		}	
	}
	
	private double parseExchangeRateToDouble(String ExchangeRateValue) {
		return Double.parseDouble(ExchangeRateValue);
	}
	
	private boolean bothParametersAreEqual(List<String> parameters) {
		if(parameters.get(0).equals(parameters.get(1)))
			return true;
		return false;
	}
	private boolean bothParametersAreDifferentFromBaseExchange(List<String> parameters) {
		if(!exchangeRateIsEqualToBaseExchange(parameters.get(0)) && !exchangeRateIsEqualToBaseExchange(parameters.get(1)))
			return true;
		return false;
	}
	private boolean secondParameterIsDifferentFromBaseExchange(List<String> parameters) {
		if(exchangeRateIsEqualToBaseExchange(parameters.get(0)) && !exchangeRateIsEqualToBaseExchange(parameters.get(1)))
			return true;
		return false;
	}
	private boolean firstParameterIsDifferentFromBaseExchange(List<String> parameters) {
		if(!exchangeRateIsEqualToBaseExchange(parameters.get(0)) && exchangeRateIsEqualToBaseExchange(parameters.get(1)))
			return true;
		return false;
	}
}

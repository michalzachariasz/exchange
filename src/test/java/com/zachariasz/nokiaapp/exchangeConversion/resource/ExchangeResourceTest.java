package com.zachariasz.nokiaapp.exchangeConversion.resource;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.AfterClass;
import org.junit.Test;

import com.zachariasz.nokiaapp.exchangeConversion.database.JPAUnit;
import com.zachariasz.nokiaapp.exchangeConversion.model.Exchange;

public class ExchangeResourceTest {
	
	private ExchangeResource exchangeResource =  new ExchangeResource();
	private final String errorDatabaseMessage = "One or more parameters does not exist in database";
	private final String errorIncorrectQuery = "Query does not appropirate form";
	private ExchangeFilterBean exchangeFilterBean = new ExchangeFilterBean();
	private static EntityManager entityManager = null;
	private EntityTransaction transaction = null;
	private List<Exchange> exchangeResultList = new ArrayList<>();
	private String addCorrectlyMessage = "Added succesfully";
	private String objectExistInDatabase = "This exchange rate alredy exist";
	private String incorrectFormatMessage =  "Incorrect format";
	private String noElementMessage = "This exchange rate does not exist in database";
	private String removedCorrectlyMessage = "Removed succesfully";
	
	@AfterClass
	public static void afterClass() {
		//SampleValues.addSampleValuesToDatabase();
	}
			
	@Test
	public void testGetAllExchanges() {
		
		
		getAllExchangesTestingMethod(exchangeResultList);
		
		addTestValueToDatabase("AAA","2.0");
		addValueToExchangesResultList("AAA", "2.0");
		getAllExchangesTestingMethod(exchangeResultList);
		
		addTestValueToDatabase("BBB","1.0");
		addValueToExchangesResultList("BBB", "1.0");
		
		getAllExchangesTestingMethod(exchangeResultList);
	
		removeTestValueFromDatabase("AAA");
		removeTestValueFromDatabase("BBB");
		removeValueToExchangesResultList("AAA", "2.0");
		removeValueToExchangesResultList("BBB", "1.0");
	}

	@Test
	public void testGetExchange() {
		
		addTestValueToDatabase("AAA","2.0");
		addTestValueToDatabase("BBB","1.0");
		
		getExchangeTestingMethod("AAA","BBB","2.0");
		getExchangeTestingMethod("BBB","AAA","0.5");
		getExchangeTestingMethod("aaa","bbb","2.0");
		getExchangeTestingMethod("AAA","PLN","2.0");
		getExchangeTestingMethod("PLN","AAA","0.5");
		getExchangeTestingMethod("PLN","PLN","1.0");
		getExchangeTestingMethod("AAA","AAA","1.0");
		getExchangeTestingMethod("CCC","CcC","1.0");
		getExchangeTestingMethod("CcC","CcC","1.0");
		getExchangeTestingMethod("CcC","CCC","1.0");
		getExchangeTestingMethod("CCC","AAA",errorDatabaseMessage);
		getExchangeTestingMethod("AAA","CCC",errorDatabaseMessage);
		getExchangeTestingMethod("AA","CCC",errorIncorrectQuery);
		getExchangeTestingMethod("AAA","CC",errorIncorrectQuery);
		getExchangeTestingMethod("AA","CC",errorIncorrectQuery);
		getExchangeTestingMethod("","",errorIncorrectQuery);
		getExchangeTestingMethod(null,null,errorIncorrectQuery);
		getExchangeTestingMethod("",null,errorIncorrectQuery);
		getExchangeTestingMethod(null,"",errorIncorrectQuery);
		getExchangeTestingMethod("AAA",null,errorIncorrectQuery);
		getExchangeTestingMethod(null,"AAA",errorIncorrectQuery);
		getExchangeTestingMethod(null,"PLN",errorIncorrectQuery);
		getExchangeTestingMethod("PLN",null,errorIncorrectQuery);
		getExchangeTestingMethod("P@N","U:S",errorIncorrectQuery);
		getExchangeTestingMethod("1@N","U:2",errorIncorrectQuery);
		
		removeTestValueFromDatabase("AAA");
		removeTestValueFromDatabase("BBB");
	}
	
	@Test
	public void testAddExchange() {
		
		addExchangeTestingMethod("AAA", "2.0", addCorrectlyMessage);
		addExchangeTestingMethod("AAA", "2.0", objectExistInDatabase);
		addExchangeTestingMethod("BBB", "1.0", addCorrectlyMessage);
		addExchangeTestingMethod("bbb", "1.0", objectExistInDatabase);
		
		removeTestValueFromDatabase("AAA");
		removeTestValueFromDatabase("BBB");
		
		addExchangeTestingMethod("BBB", "AA", incorrectFormatMessage);
		addExchangeTestingMethod("BB", "2.0", incorrectFormatMessage);
		addExchangeTestingMethod("BBB", "AA", incorrectFormatMessage);
		addExchangeTestingMethod(null, null, incorrectFormatMessage);
		addExchangeTestingMethod("BBB", null, incorrectFormatMessage);
		addExchangeTestingMethod(null, "2.0", incorrectFormatMessage);
		addExchangeTestingMethod("AA@", "2.0", incorrectFormatMessage);
		addExchangeTestingMethod("AA1", "2.0", incorrectFormatMessage);
	}
	
	@Test
	public void testDeleteExchange() {
		
		addTestValueToDatabase("AAA","2.0");
		addTestValueToDatabase("BBB","1.0");
		
		deleteExchangeTestingMethod("AAA", removedCorrectlyMessage);
		deleteExchangeTestingMethod("AAA", noElementMessage);
		deleteExchangeTestingMethod("bbb", removedCorrectlyMessage);
		deleteExchangeTestingMethod("BBB", noElementMessage);
		deleteExchangeTestingMethod(null, incorrectFormatMessage);
		deleteExchangeTestingMethod("", incorrectFormatMessage);
		deleteExchangeTestingMethod("A", incorrectFormatMessage);
		deleteExchangeTestingMethod("AA", incorrectFormatMessage);
		deleteExchangeTestingMethod("AA1", incorrectFormatMessage);
		deleteExchangeTestingMethod("AA@", incorrectFormatMessage);
	}



	

	private void removeTestValueFromDatabase(String exchangeRateName) {
		entityManager = JPAUnit.getEntityManagerFactory().createEntityManager();
		try {
			transaction = entityManager.getTransaction();
			transaction.begin();
			entityManager.remove(entityManager.find(Exchange.class,exchangeRateName));
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			System.out.println("Something goes wrong during removing test values: " + e.getMessage());
		} finally {
			entityManager.close();
		}
	}

	private void addTestValueToDatabase(String exchangeRateName, String exchangeRateValue) {
		entityManager = JPAUnit.getEntityManagerFactory().createEntityManager();
		try {
			transaction = entityManager.getTransaction();
			transaction.begin();
			entityManager.persist(new Exchange(exchangeRateName,exchangeRateValue));
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			System.out.println("Something goes wrong during adding test values" + e.getMessage());
		} finally {
			entityManager.close();
		}
	}
	
	private void getExchangeTestingMethod(String firstExchangeRate, String secondExchangeRate, String desiredResult) {
		exchangeFilterBean.setFirstExchangeValue(firstExchangeRate);
		exchangeFilterBean.setSecondExchangeValue(secondExchangeRate);
		String exchange = exchangeResource.getExchange(exchangeFilterBean);
		assertEquals(desiredResult, exchange);
	}
	
	private void getAllExchangesTestingMethod(List<Exchange> desiredResult) {
		List<Exchange> allExchanges = exchangeResource.getAllExchanges();
		assertEquals(desiredResult, allExchanges);
	}
	private void addValueToExchangesResultList(String exchangeRateName, String exchangeRateValue) {
		exchangeResultList.add(new Exchange(exchangeRateName, exchangeRateValue));
	}
	
	private void removeValueToExchangesResultList(String exchangeRateName, String exchangeRateValue) {
		exchangeResultList.remove(new Exchange(exchangeRateName, exchangeRateValue));
	}
	
	private void addExchangeTestingMethod(String exchangeRateName, String exchangeRateValue, String message) {
		String addExchange = exchangeResource.addExchange(new Exchange(exchangeRateName, exchangeRateValue));
		assertEquals(message, addExchange);
	}
	
	private void deleteExchangeTestingMethod(String exchangeRateName,String message) {
		String deleteExchange = exchangeResource.deleteExchange(exchangeRateName);
		assertEquals(message, deleteExchange);
	}

}

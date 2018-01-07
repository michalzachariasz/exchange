package com.zachariasz.nokiaapp.exchangeConversion;

import javax.persistence.EntityManager;

import com.zachariasz.nokiaapp.exchangeConversion.database.JPAUnit;


public class Main {
	/* Please uncomment  <property name="hibernate.hbm2ddl.auto" value="create"/> inside
	 * .../exchangeConversion/src/main/resources/META-INF/persistence.xml  and right-click -> Run As -> Java Application
	 * This method will create table in database*/
	
	public static void main(String[] args) {
		EntityManager entityManager = JPAUnit.getEntityManagerFactory().createEntityManager();
		
		entityManager.close();
		JPAUnit.shutdownEntityManagerFactory();
	}
	// When main method will finish please comment <property name="hibernate.hbm2ddl.auto" value="create"/> inside presistence.xml
	// After that You can build Maven Project
}

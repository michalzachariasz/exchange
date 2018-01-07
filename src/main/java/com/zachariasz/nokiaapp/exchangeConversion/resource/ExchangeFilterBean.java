package com.zachariasz.nokiaapp.exchangeConversion.resource;

import javax.ws.rs.QueryParam;

public class ExchangeFilterBean {
	private @QueryParam("FEI") String firstExchangeRate;
	private @QueryParam("SEI") String secondExchangeRate;
	
	public String getFirstExchangeValue() {
		return firstExchangeRate;
	}
	public void setFirstExchangeValue(String firstExchangeValue) {
		this.firstExchangeRate = firstExchangeValue;
	}
	public String getSecondExchangeValue() {
		return secondExchangeRate;
	}
	public void setSecondExchangeValue(String secondExchangeValue) {
		this.secondExchangeRate = secondExchangeValue;
	}
}

package com.zachariasz.nokiaapp.exchangeConversion.service;

import java.util.List;

import com.zachariasz.nokiaapp.exchangeConversion.model.Exchange;

public interface ExchangeServiceGetInterface {
	List<Exchange> getAllExchanges();
	String getExchange(List<String> parameters);
}

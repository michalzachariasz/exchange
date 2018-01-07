package com.zachariasz.nokiaapp.exchangeConversion.service;

import com.zachariasz.nokiaapp.exchangeConversion.model.Exchange;

public interface ExchangeServiceWihoutGetInterface {
	 String addExchange(Exchange exchange);
	 String deleteExchange(String exchangeRateName);
}

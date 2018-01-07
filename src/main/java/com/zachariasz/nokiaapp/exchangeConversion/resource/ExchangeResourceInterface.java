package com.zachariasz.nokiaapp.exchangeConversion.resource;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.QueryParam;

import com.zachariasz.nokiaapp.exchangeConversion.model.Exchange;

public interface ExchangeResourceInterface {
	List<Exchange> getAllExchanges();
	String getExchange(@BeanParam ExchangeFilterBean exchangeFilter);
	String addExchange(Exchange exchange);
	String deleteExchange(@QueryParam("DEL") String exchangeRateName);
}

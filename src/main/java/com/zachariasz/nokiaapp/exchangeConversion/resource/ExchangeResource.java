package com.zachariasz.nokiaapp.exchangeConversion.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.zachariasz.nokiaapp.exchangeConversion.model.Exchange;
import com.zachariasz.nokiaapp.exchangeConversion.service.ExchangeServiceGet;
import com.zachariasz.nokiaapp.exchangeConversion.service.ExchangeServiceWithoutGet;

@Path("/exchanges")
public class ExchangeResource implements ExchangeResourceInterface {

	private ExchangeServiceGet exchangeService = null;
	private ExchangeServiceWithoutGet exchangeServiceWithoutGet = null;

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Exchange> getAllExchanges() {
		exchangeService = new ExchangeServiceGet();
		return exchangeService.getAllExchanges();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public String getExchange(@BeanParam ExchangeFilterBean exchangeFilter) {
		if (exchangeRatesHaveProperForm(exchangeFilter)) {
			exchangeService = new ExchangeServiceGet();
			return exchangeService.getExchange(createListAndInsertValues(exchangeFilter));
		}
		return "Query does not appropirate form";
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addExchange(Exchange exchange) {
		if (exchangeRateHasProperForm(exchange.getId()) && ableToParseToDoubleAndNotNull(exchange.getValue())) {
			exchangeServiceWithoutGet = new ExchangeServiceWithoutGet();
			return exchangeServiceWithoutGet.addExchange(exchange);
		}
		return "Incorrect format";
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public String deleteExchange(@QueryParam("DEL") String exchangeRateName) {
		if (exchangeRateHasProperForm(exchangeRateName)) {
			exchangeServiceWithoutGet = new ExchangeServiceWithoutGet();
			return exchangeServiceWithoutGet.deleteExchange(exchangeRateName);
		}
		return "Incorrect format";
	}

	private boolean ableToParseToDoubleAndNotNull(String ExchangeRateValue) {
		if (ExchangeRateValue != null) {
			try {
				Double.parseDouble(ExchangeRateValue);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}

	private boolean exchangeRateHasProperForm(String index) {
		if (index == null || index.length() != 3 || !Pattern.matches("[a-zA-Z]+", index))
			return false;
		return true;
	}

	private boolean exchangeRatesHaveProperForm(ExchangeFilterBean exchangeFilter) {
		if (exchangeRateHasProperForm(exchangeFilter.getFirstExchangeValue())
				&& exchangeRateHasProperForm(exchangeFilter.getSecondExchangeValue()))
			return true;
		return false;
	}

	private List<String> createListAndInsertValues(ExchangeFilterBean exchangeFilter) {
		List<String> parameters = new ArrayList<>();
		parameters.add(exchangeFilter.getFirstExchangeValue().toUpperCase());
		parameters.add(exchangeFilter.getSecondExchangeValue().toUpperCase());
		return parameters;
	}
}

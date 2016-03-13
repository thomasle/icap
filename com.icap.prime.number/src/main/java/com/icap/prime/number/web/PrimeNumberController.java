/**
 * 
 */
package com.icap.prime.number.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icap.prime.number.domain.PrimeNumberResponse;
import com.icap.prime.number.service.PrimeNumberService;

/**
 * @author thomas
 *
 */
@RestController
public class PrimeNumberController {
	@Inject
	private PrimeNumberService service;

	@RequestMapping(value = "/{maxRange}", produces = { "application/json" })
	PrimeNumberResponse calculate(@PathVariable int maxRange) {
		return new PrimeNumberResponse(maxRange, service.findPrimes(maxRange));
	}

	@RequestMapping(value = "/stream/{maxRange}", produces = { "application/json" })
	String calculateAndOutputStream(@PathVariable int maxRange, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write("{ \"maxRange\":".getBytes());
		response.getOutputStream().write(String.valueOf(maxRange).getBytes());
		response.getOutputStream().write(", \"primes\":[".getBytes());
		service.findPrimes(maxRange, response.getOutputStream());
		response.getOutputStream().write("] }".getBytes());
		response.getOutputStream().flush();
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		return "";
	}
}

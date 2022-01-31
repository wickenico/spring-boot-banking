package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when interest rate is lower equal then zero
 * 
 * @author nicowickersheim
 *
 */

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED, reason = "Interest rate must be greater zero.")
public class InterestRateZeroException extends Exception {

	/**
	 * @param message
	 */
	public InterestRateZeroException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}

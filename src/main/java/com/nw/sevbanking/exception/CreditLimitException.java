package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when the credit limit is reached.
 * @author nicowickersheim
 *
 */

@ResponseStatus(code=HttpStatus.PRECONDITION_FAILED, reason = "Credit limit reached and cannot be exceeded.")
public class CreditLimitException extends Exception {

	/**
	 * @param message
	 */
	public CreditLimitException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	
	
}

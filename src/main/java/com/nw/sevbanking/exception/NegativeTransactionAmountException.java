package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when the amount of a transaction between accounts is negative
 * @author nicowickersheim
 *
 */

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED, reason = "Cannot transfer a negative amount.")
public class NegativeTransactionAmountException extends Exception {

	/**
	 * @param message
	 */
	public NegativeTransactionAmountException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}

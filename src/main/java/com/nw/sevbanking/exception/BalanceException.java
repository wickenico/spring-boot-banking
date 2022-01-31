package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when the balance is to low for transactions
 * @author nicowickersheim
 *
 */

@ResponseStatus(code=HttpStatus.PRECONDITION_FAILED, reason = "Balance to low.")
public class BalanceException extends Exception{

	/**
	 * @param message
	 */
	public BalanceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	

}

package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when the monthly limit of Fixed Deposit Account is reached
 * 
 * @author nicowickersheim
 *
 */

@ResponseStatus(code=HttpStatus.CONFLICT, reason = "Balance too low.")
public class FixedDepositMonthLimitException extends Exception {
	
	/**
	 * Constructor
	 * @param msg
	 */
	public FixedDepositMonthLimitException(String msg) {
		super(msg);
	}

}

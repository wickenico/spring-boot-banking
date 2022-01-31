package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when Balance of Fixed Deposite Account is negative.
 * @author nicowickersheim
 *
 */

@ResponseStatus(code=HttpStatus.CONFLICT, reason = "Account balance cannot go into the negative.")
public class FixedDepositNegativeBalanceException extends Exception {

	/**
	 * Constructor
	 * @param msg
	 */
	public FixedDepositNegativeBalanceException(String msg) {
		super(msg);
	}
	
}

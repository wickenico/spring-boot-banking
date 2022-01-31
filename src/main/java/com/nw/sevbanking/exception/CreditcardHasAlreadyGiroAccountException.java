package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when the creditcard already has giro account
 * @author nicowickersheim
 *
 */

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED, reason = "Creditcard already has a giro account.")
public class CreditcardHasAlreadyGiroAccountException extends Exception {
	/**
	 * 
	 * @param msg
	 */
	public CreditcardHasAlreadyGiroAccountException(String msg) {
		super();
	}
}

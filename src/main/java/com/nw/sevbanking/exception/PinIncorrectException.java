package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when the authorization of HTTP failed
 * 
 * @author nicowickersheim
 */

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "PIN not valid")
public class PinIncorrectException extends Exception {

	/**
	 * Constructor
	 * 
	 * @param msg
	 */
	public PinIncorrectException(String msg) {
		super(msg);
	}

}

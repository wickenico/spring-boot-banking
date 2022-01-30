package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when the database id isnt found
 * @author nicowickersheim
 * @date 29.11.2022
 */
@ResponseStatus(code=HttpStatus.FORBIDDEN, reason = "PIN not valid")
public class PinIncorrectException extends Exception{

	/**
	 * Constructor
	 * @param msg
	 */
	public PinIncorrectException(String msg) {
		super(msg);
	}
	
}

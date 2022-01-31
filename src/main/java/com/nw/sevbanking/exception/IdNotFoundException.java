package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when the database cannot find id
 * @author nicowickersheim
 * 
 */
@ResponseStatus(code=HttpStatus.NOT_FOUND, reason = "Database ID not found")
public class IdNotFoundException extends Exception{

	/**
	 * Constructor
	 * @param msg
	 */
	public IdNotFoundException(String msg) {
		super(msg);
	}
	
}

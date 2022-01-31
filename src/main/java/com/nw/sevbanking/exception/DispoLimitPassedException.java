package com.nw.sevbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when the database id isn't found
 * @author nicowickersheim
 * 
 */

@ResponseStatus(code=HttpStatus.CONFLICT, reason = "Database ID not found")
public class DispoLimitPassedException extends Exception{

	/**
	 * Constructor
	 * @param msg
	 */
	public DispoLimitPassedException(String msg) {
		super(msg);
	}
	
}

package com.nw.sevbanking.api.v1.apimodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransactionModel {

	/**
	 * Amount for the transaction
	 */
	private long amount;
	
	/**
	 * PIN for the account
	 */
	private int pin;
	
}

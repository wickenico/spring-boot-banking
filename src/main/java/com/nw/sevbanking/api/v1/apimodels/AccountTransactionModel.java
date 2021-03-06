package com.nw.sevbanking.api.v1.apimodels;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for Account Transaction
 * 
 * @author nicowickersheim
 *
 */

@Getter
@Setter
public class AccountTransactionModel {

	/**
	 * Amount for the transaction
	 */
	private long amount;

}

package com.nw.sevbanking.api.v1.apimodels;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for Creditcard Transaction
 * 
 * @author nicowickersheim
 *
 */

@Getter
@Setter
public class CreditcardTransactionModel {

	/**
	 * Amount for the transaction
	 */
	private long amount;

}

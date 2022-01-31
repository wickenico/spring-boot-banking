package com.nw.sevbanking.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * Credit card class
 * @author nicowickersheim
 * 
 */

@Getter
@Setter
@Entity
@Table(name="Creditcards")
public class CreditcardDTO {

	/**
	 * Primary key for the creditcard (creditcard number)
	 */
	@Id
	@GenericGenerator(name = "creditcard_number", strategy = "com.nw.sevbanking.generator.CreditcardNumberGenerator")
	@GeneratedValue(generator = "creditcard_number") 
	private long creditcardNumber;
	
	/**
	 * Credit limit for this creditcard
	 */
	private long creditLimit;
	
	/**
	 * Output amount for this creditcard which is transfered to giro account
	 */
	private long outputAmount = 0;
	
	/**
	 * Related giro account for this creditcard
	 */
	@ManyToOne
	private GiroAccountDTO giroAccount;
	
}

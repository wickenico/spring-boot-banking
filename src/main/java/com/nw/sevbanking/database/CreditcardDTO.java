package com.nw.sevbanking.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Credit card class
 * @author nicowickersheim
 * @date 29.11.2022
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long creditcardNumber;
	
	/**
	 * Credit limit for this creditcard
	 */
	private long creditLimit;
	
	/**
	 * Related giro account for this creditcard
	 */
	@ManyToOne
	private GiroAccountDTO giroAccount;
	
}

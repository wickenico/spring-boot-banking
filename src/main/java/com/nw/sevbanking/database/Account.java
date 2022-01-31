package com.nw.sevbanking.database;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract account class
 * --> Every table which inherits from this abstract class generates a own database table
 * --> Annotation MappedSuperClass is used (no separate account table should exist in the database)
 * @author nicowickersheim
 * 
 */
@MappedSuperclass
@Getter
@Setter
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Account {

	/**
	 * Primary key of the database table
	 */
	@Id
	@GenericGenerator(name = "account_id", strategy = "com.nw.sevbanking.generator.AccountIdGenerator")
	@GeneratedValue(generator = "account_id", strategy = GenerationType.IDENTITY) 
	@Column(insertable = false, updatable = false)
	private long accountId;
	
	/**
	 * Actual balance of the account in cent
	 * Used long because float and double generates error in rounding of floating numbers
	 */
	private long balance = 0;
	
	/**
	 * Name of the account
	 */
	private String accountName;
	
	/**
	 * Pin of the account
	 * Assume that the pin is equal to real life scenarios
	 */
	@JsonProperty(access=Access.WRITE_ONLY)
	private int pin;
	
	/**
	 * Dispo limit of the account in cent
	 * Used long because float and double generates error in rounding of floating numbers
	 */
	
	private long dispoLimit;
	
	
	/**
	 * Transaction log for an account
	 */
	@OneToMany(cascade=CascadeType.ALL)
	private List<TransactionDTO> transaction = new ArrayList<>();
}

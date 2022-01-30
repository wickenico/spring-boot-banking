package com.nw.sevbanking.database;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="Transactions")
public class TransactionDTO {

	/**
	 * Primary key
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Id;
	
	/**
	 * Amount of the transaction
	 * could be negativ or positiv
	 */
	private long amount;
	
	/**
	 * Date of the transaction
	 */
	@CreationTimestamp
	private Date transactionDate;

	/**
	 * 
	 */
	public TransactionDTO() {
		super();
	}
	
	/**
	 * @param amount
	 * @param transactionDate
	 */
	public TransactionDTO(long amount) {
		super();
		this.amount = amount;
		this.transactionDate = transactionDate;
	}


	
	
	
//	/**
//	 * From account
//	 */
//	@OneToOne
//	private Account fromAccount;
//	
//	/**
//	 * To Account
//	 */
//	@OneToOne
//	private Account toAccount;
}

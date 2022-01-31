package com.nw.sevbanking.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * Customer Class 
 * Represents the DTO (Database Table Object)
 * @author nicowickersheim
 * @date 29.01.2022
 */
@Getter
@Setter
@Entity
@Table(name="Customers")
public class CustomerDTO {

	/**
	 * Customer ID (Primary Key for the Database)
	 */
	@Id
	@GenericGenerator(name = "customer_id", strategy = "com.nw.sevbanking.generator.CustomerIdGenerator")
	@GeneratedValue(generator = "customer_id")  
	private long customerId;
	
	/**
	 * Name of the User / Customer
	 */
	private String name;
	
	/**
	 * First name of the User / Customer
	 */
	private String firstName;
	
	/**
	 * Gender of the User / Customer
	 */
	private String gender;
	
	/**
	 * Birthdate of the User / Customer
	 */
	private Date birthdate;
	
	/**
	 * Related giro accounts
	 */
	@ManyToMany
	private List<GiroAccountDTO> giroAccounts = new ArrayList<>();
	
	/**
	 * Related fixed deposit accounts
	 */
	@ManyToMany
	private List<FixedDepositAccountDTO> fixedDepositAccounts = new ArrayList<>();
}

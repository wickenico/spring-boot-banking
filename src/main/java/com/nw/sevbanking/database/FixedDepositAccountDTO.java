package com.nw.sevbanking.database;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

/**
 * Fixed deposit account class
 * @author nicowickersheim
 * @date 29.01.2022
 */
@Getter
@Setter
@Entity
@Table(name="FixedDepositAccounts")
public class FixedDepositAccountDTO extends Account{

	/**
	 * Rate of interest
	 */
	@Column(nullable=false)
	private double interestRate;
	
	
	/**
	 * Users for a account
	 */
	@ManyToMany(mappedBy = "fixedDepositAccounts")
	@JsonBackReference
	private List<CustomerDTO> customers = new ArrayList<>();
}

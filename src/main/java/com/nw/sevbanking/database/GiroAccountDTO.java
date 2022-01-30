package com.nw.sevbanking.database;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

/**
 * Giro account class
 * @author nicowickersheim
 * @date 29.01.2022
 */
@Getter
@Setter
@Entity
@Table(name="GiroAccounts")
public class GiroAccountDTO extends Account{

	/**
	 * Connected credit cards to a giro account
	 */
	@OneToMany
	private List<CreditcardDTO> creditcards = new ArrayList<>();
	
	/**
	 * Users for a account
	 */
	@ManyToMany(mappedBy = "giroAccounts")
	@JsonBackReference
	private List<CustomerDTO> customers = new ArrayList<>();
	
	// TODO: Set anstatt List
}

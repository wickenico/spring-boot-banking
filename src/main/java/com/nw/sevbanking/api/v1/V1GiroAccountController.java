package com.nw.sevbanking.api.v1;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nw.sevbanking.api.v1.apimodels.AccountTransactionModel;
import com.nw.sevbanking.database.CustomerDTO;
import com.nw.sevbanking.database.GiroAccountDTO;
import com.nw.sevbanking.database.TransactionDTO;
import com.nw.sevbanking.exception.DispoLimitPassedException;
import com.nw.sevbanking.exception.IdNotFoundException;
import com.nw.sevbanking.exception.PinIncorrectException;
import com.nw.sevbanking.repository.ICustomerRepository;
import com.nw.sevbanking.repository.IGiroAccountRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="/api/v1/giroaccounts")
@RequiredArgsConstructor
public class V1GiroAccountController {

	@Autowired
	private IGiroAccountRepository giroAccountRepository;
	
	@Autowired
	private ICustomerRepository customerRepository;
	
	/**
	 * Creates a new giro account
	 * @param giroAccount
	 * @return Giro Account DTO
	 */
	@Operation(summary = "Create a giroaccount with random acoount id.")
	@PostMapping(consumes= {"application/json"})
	public GiroAccountDTO CreateGiroAccount(@RequestBody GiroAccountDTO giroAccount) {
		giroAccount.setBalance(0);
		giroAccountRepository.save(giroAccount);
		return giroAccount;
	}
	
	/**
	 * Read a specific Giro Account
	 * @param id
	 * @return Giro Account
	 * @throws IdNotFoundException  - HTTP Status Code 404 ID not found
	 */
	@GetMapping(value= "{id}")
	public GiroAccountDTO ReadGiroAccount(@PathVariable long id) throws IdNotFoundException {
		Optional<GiroAccountDTO> giroAccount = giroAccountRepository.findById(id);
		if(giroAccount.isPresent()) {
			return giroAccount.get();
		}
		
		throw new IdNotFoundException("No giro account with the " + id + " found.");
	}
	
	/**
	 * Updates a given Giro Account
	 * @param id
	 * @param customer
	 * @return
	 * @throws IdNotFoundException - HTTP Status Code 404 ID not found
	 */
	@PutMapping(value="{id}")
	public GiroAccountDTO UpdateGiroAccount(@PathVariable long id, @RequestBody GiroAccountDTO giroAccount) throws IdNotFoundException {
		GiroAccountDTO databaseGiroAccount = giroAccountRepository.findById(id).orElseThrow(() ->  new IdNotFoundException("No giro account with the id " + id + " found."));

		databaseGiroAccount.setAccountName(giroAccount.getAccountName());
		databaseGiroAccount.setBalance(giroAccount.getBalance());
		databaseGiroAccount.setPin(giroAccount.getPin());
		databaseGiroAccount.setDispoLimit(giroAccount.getDispoLimit());
		
		giroAccountRepository.save(databaseGiroAccount);
	    return databaseGiroAccount;
	}
	
	@PostMapping(value="{id}/transaction")
	public GiroAccountDTO AddTransaction(@PathVariable long id, @RequestBody AccountTransactionModel transaction) throws IdNotFoundException, PinIncorrectException, DispoLimitPassedException {
		GiroAccountDTO databaseGiroAccount = giroAccountRepository.findById(id).orElseThrow(() -> new IdNotFoundException("No giro account with the id " + id + " found."));
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		CustomerDTO customer = customerRepository.findById(Long.parseLong(auth.getName())).orElseThrow(() -> new IdNotFoundException("Id not found."));
		if(!giroAccountRepository.existsByAccountIdAndCustomers_customerId(databaseGiroAccount.getAccountId(), customer.getCustomerId())) {
			throw new IdNotFoundException("Id not found.");
		}
		
//		if(!auth.getCredentials().equals(String.valueOf(databaseGiroAccount.getPin()))) {
//			throw new PinIncorrectException("Pin not valid");
//		}
//		
		if((databaseGiroAccount.getBalance()+transaction.getAmount())<(databaseGiroAccount.getDispoLimit()*-1)){
			throw new DispoLimitPassedException("Dispo limit passed.");
		}
		
		databaseGiroAccount.setBalance(databaseGiroAccount.getBalance()+transaction.getAmount());
		
		if(databaseGiroAccount.getTransaction()==null) {
	    	List<TransactionDTO> tempTransactions = new ArrayList<>();
	    	tempTransactions.add(new TransactionDTO(transaction.getAmount()));
	    	databaseGiroAccount.setTransaction(tempTransactions);
	    }else {
	    	List<TransactionDTO> tempTransactions = databaseGiroAccount.getTransaction();
	    	tempTransactions.add(new TransactionDTO(transaction.getAmount()));
	    }
		
		giroAccountRepository.save(databaseGiroAccount);
		
		return databaseGiroAccount;
		
	}
	
}


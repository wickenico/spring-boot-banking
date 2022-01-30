package com.nw.sevbanking.api.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nw.sevbanking.database.CustomerDTO;
import com.nw.sevbanking.database.FixedDepositAccountDTO;
import com.nw.sevbanking.database.GiroAccountDTO;
import com.nw.sevbanking.exception.IdNotFoundException;
import com.nw.sevbanking.repository.ICustomerRepository;
import com.nw.sevbanking.repository.IFixedDepositAccountRepository;
import com.nw.sevbanking.repository.IGiroAccountRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="/api/v1/customers")
@RequiredArgsConstructor
public class V1CustomerController {

	@Autowired
	private ICustomerRepository customerRepository;
	
	@Autowired
	private IFixedDepositAccountRepository fixedDepositAccountRepository;
	
	@Autowired
	private IGiroAccountRepository giroAccountRepository;
	
	/**
	 * Creates a new Customer
	 * @param customer
	 * @return customer
	 */
	@PostMapping(consumes= {"application/json"})
	public CustomerDTO CreateUser(@RequestBody CustomerDTO customer)  {
		customer = customerRepository.save(customer);
		return customer;
	}
	
	/**
	 * Read a specific User
	 * @param id
	 * @return customer
	 * @throws IdNotFoundException  - HTTP Status Code 404 ID not found
	 */
	@GetMapping(value= "{id}")
	public CustomerDTO ReadUser(@PathVariable long id) throws IdNotFoundException {
		Optional<CustomerDTO> customer = customerRepository.findById(id);
		if(customer.isPresent()) {
			return customer.get();
		}
		
		throw new IdNotFoundException("No customer with the " + id + " found.");
	}
	
	/**
	 * Updates a given User
	 * @param id
	 * @param customer
	 * @return
	 * @throws IdNotFoundException - HTTP Status Code 404 ID not found
	 */
	@PutMapping(value="{id}")
	public CustomerDTO UpdateUser(@PathVariable long id, @RequestBody CustomerDTO customer) throws IdNotFoundException {
		Optional<CustomerDTO> databaseCustomer = customerRepository.findById(id);
		if(databaseCustomer.isPresent()) {
            customerRepository.save(customer);
		}
		throw new IdNotFoundException("No customer with the id " + id + " found.");
	}
	
	
	/**
	 * 
	 * @param customerId
	 * @param fixedDepositId
	 * @return
	 * @throws IdNotFoundException 
	 */
	@PutMapping(value= "{customerId}/depositaccount/{fixedDepositId}")
	public CustomerDTO AddFixedDepositAccountToCustomer(@PathVariable long customerId, @PathVariable long fixedDepositId) throws IdNotFoundException {
		Optional<CustomerDTO> databaseCustomer = customerRepository.findById(customerId);
		
		
		if(databaseCustomer.isEmpty()) {
			throw new IdNotFoundException("No customer with the id "+ customerId +" found.");
		}
		
		Optional<FixedDepositAccountDTO> databaseFixedDepositAccount = fixedDepositAccountRepository.findById(fixedDepositId);
		if(databaseFixedDepositAccount.isEmpty()) {
			throw new IdNotFoundException("No fixed deposit account with the id " + fixedDepositId + " found.");
		}

		var tempCustomer = databaseCustomer.get();
		var tempDepositAccount = databaseFixedDepositAccount.get();
		
	    if(tempCustomer.getFixedDepositAccounts()==null) {
	    	List<FixedDepositAccountDTO> tempDepositAccounts = new ArrayList<>();
	    	tempDepositAccounts.add(tempDepositAccount);
	    }else {
	    	List<FixedDepositAccountDTO> tempDepositAccounts = tempCustomer.getFixedDepositAccounts();
	    	tempDepositAccounts.add(tempDepositAccount);
	    }
	    
	    customerRepository.save(tempCustomer);
	    
	    return tempCustomer;
	
	}
	
	/**
	 * 
	 * @param customerId
	 * @param fixedDepositId
	 * @return
	 * @throws IdNotFoundException 
	 */
	@PutMapping(value= "{customerId}/giroaccount/{giroAccountId}")
	public CustomerDTO AddGiroAccountToCustomer(@PathVariable long customerId, @PathVariable long giroAccountId) throws IdNotFoundException {
		Optional<CustomerDTO> databaseCustomer = customerRepository.findById(customerId);
		
		
		if(databaseCustomer.isEmpty()) {
			throw new IdNotFoundException("No customer with the id "+ customerId +" found.");
		}
		
		Optional<GiroAccountDTO> databaseGiroAccount = giroAccountRepository.findById(giroAccountId);
		if(databaseGiroAccount.isEmpty()) {
			throw new IdNotFoundException("No fixed deposit account with the id " + giroAccountId + " found.");
		}

		var tempCustomer = databaseCustomer.get();
		var tempGiroAccount = databaseGiroAccount.get();
		
	    if(tempCustomer.getGiroAccounts()==null) {
	    	List<GiroAccountDTO> tempGiroAccounts = new ArrayList<>();
	    	tempGiroAccounts.add(tempGiroAccount);
	    }else {
	    	List<GiroAccountDTO> tempGiroAccounts = tempCustomer.getGiroAccounts();
	    	tempGiroAccounts.add(tempGiroAccount);
	    }
	    
	    customerRepository.save(tempCustomer);
	    
	    return tempCustomer;
	
	}
	
	
	
}

package com.nw.sevbanking.api.v1;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nw.sevbanking.database.CreditcardDTO;
import com.nw.sevbanking.database.CustomerDTO;
import com.nw.sevbanking.exception.IdNotFoundException;
import com.nw.sevbanking.repository.ICreditcardRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="/api/v1/creditcards")
@RequiredArgsConstructor
public class V1CreditcardController {
	
	@Autowired
	private ICreditcardRepository creditcardRepository;
	
	/**
	 * Creates a new Creditcard
	 * @param creditcard
	 * @return creditcard
	 */
	@PostMapping(consumes= {"application/json"})
	public CreditcardDTO CreateCreditcard(@RequestBody CreditcardDTO creditcard)  {
		creditcard = creditcardRepository.save(creditcard);
		return creditcard;
	}
	
	/**
	 * Read a specific Creditcard
	 * @param id
	 * @return Creditcard
	 * @throws IdNotFoundException  - HTTP Status Code 404 ID not found
	 */
	@GetMapping(value= "{id}")
	public CreditcardDTO ReadCreditcard(@PathVariable long id) throws IdNotFoundException {
		Optional<CreditcardDTO> creditcard = creditcardRepository.findById(id);
		if(creditcard.isPresent()) {
			return creditcard.get();
		}
		
		throw new IdNotFoundException("No customer with the " + id + " found.");
	}
	
	/**
	 * Updates a given Creditcard
	 * @param id
	 * @param Creditcard
	 * @return
	 * @throws IdNotFoundException - HTTP Status Code 404 ID not found
	 */
	@PutMapping(value="{id}")
	public CreditcardDTO UpdateUser(@PathVariable long id, @RequestBody CreditcardDTO creditcard) throws IdNotFoundException {
		Optional<CreditcardDTO> databaseCustomer = creditcardRepository.findById(id);
		if(databaseCustomer.isPresent()) {
			creditcardRepository.save(creditcard);
		}
		throw new IdNotFoundException("No customer with the id " + id + " found.");
	}
	
}

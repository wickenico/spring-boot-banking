package com.nw.sevbanking.api.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nw.sevbanking.api.v1.apimodels.CreditcardTransactionModel;
import com.nw.sevbanking.database.CreditcardDTO;
import com.nw.sevbanking.database.GiroAccountDTO;
import com.nw.sevbanking.database.TransactionDTO;
import com.nw.sevbanking.exception.CreditLimitException;
import com.nw.sevbanking.exception.DispoLimitPassedException;
import com.nw.sevbanking.exception.IdNotFoundException;
import com.nw.sevbanking.repository.ICreditcardRepository;
import com.nw.sevbanking.repository.IGiroAccountRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for Creditcard methods.
 * 
 * @author nicowickersheim
 *
 */

@RestController
@RequestMapping(path = "/api/v1/creditcards")
@RequiredArgsConstructor
public class V1CreditcardController {

	@Autowired
	private ICreditcardRepository creditcardRepository;

	@Autowired
	private IGiroAccountRepository giroAccountRepository;

	/**
	 * Creates a new Creditcard
	 * 
	 * @param creditcard
	 * @return creditcard
	 */
	@Operation(summary = "Create a creditcard with random creditcard number.")
	@PostMapping
	public CreditcardDTO CreateCreditcard(@RequestBody CreditcardDTO creditcard) {
		creditcard = creditcardRepository.save(creditcard);
		return creditcard;
	}

	/**
	 * Read a specific Creditcard
	 * 
	 * @param id
	 * @return Creditcard
	 * @throws IdNotFoundException - HTTP Status Code 404 ID not found
	 */
	@Operation(summary = "Read a specific creditcard.")
	@GetMapping(value = "{id}")
	public CreditcardDTO ReadCreditcard(@PathVariable long id) throws IdNotFoundException {
		CreditcardDTO creditcard = creditcardRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No creditcard with the id " + id + " found."));
		return creditcard;

	}

	/**
	 * Updates a given Creditcard
	 * 
	 * @param id
	 * @param Creditcard
	 * @return
	 * @throws IdNotFoundException - HTTP Status Code 404 ID not found
	 */
	@Operation(summary = "Update a specific creditcard.")
	@PutMapping(value = "{id}")
	public CreditcardDTO UpdateCreditcard(@PathVariable long id, @RequestBody CreditcardDTO creditcard)
			throws IdNotFoundException {
		CreditcardDTO databaseCreditcard = creditcardRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No creditcard with the id " + id + " found."));

		databaseCreditcard.setCreditLimit(creditcard.getCreditLimit());
		creditcardRepository.save(databaseCreditcard);
		return databaseCreditcard;
	}

	/**
	 * Creates a transaction and transfer amount on creditcard output and
	 * giroaccount balance
	 * 
	 * @param id
	 * @param transaction
	 * @return
	 * @throws IdNotFoundException
	 * @throws DispoLimitPassedException
	 * @throws CreditLimitException
	 */
	@Operation(summary = "Add transaction and transfer amount on creditcard output and giro account balance.")
	@PostMapping("{id}/transaction")
	public CreditcardDTO AddTransaction(@PathVariable long id, @RequestBody CreditcardTransactionModel transaction)
			throws IdNotFoundException, DispoLimitPassedException, CreditLimitException {
		CreditcardDTO databaseCreditcard = creditcardRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No creditcard with the id " + id + " found."));
		GiroAccountDTO databaseGiroAccount = giroAccountRepository
				.findById(databaseCreditcard.getGiroAccount().getAccountId())
				.orElseThrow(() -> new IdNotFoundException("No giro account with the id " + id + " found."));

		if (databaseCreditcard.getCreditLimit() < (databaseCreditcard.getOutputAmount() + transaction.getAmount())) {
			throw new CreditLimitException("Credit limit reached and cannot be exceeded.");
		}

		databaseCreditcard.setOutputAmount(databaseCreditcard.getOutputAmount() + transaction.getAmount());
		databaseGiroAccount.setBalance(databaseGiroAccount.getBalance() + transaction.getAmount());

		if (databaseGiroAccount.getTransaction() == null) {
			List<TransactionDTO> tempTransactions = new ArrayList<>();
			tempTransactions.add(new TransactionDTO(transaction.getAmount()));
			databaseGiroAccount.setTransaction(tempTransactions);
		} else {
			List<TransactionDTO> tempTransactions = databaseGiroAccount.getTransaction();
			tempTransactions.add(new TransactionDTO(transaction.getAmount()));
		}

		creditcardRepository.save(databaseCreditcard);
		giroAccountRepository.save(databaseGiroAccount);

		return databaseCreditcard;

	}

}

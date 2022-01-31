package com.nw.sevbanking.api.v1;

import java.util.ArrayList;
import java.util.List;

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
import com.nw.sevbanking.database.CreditcardDTO;
import com.nw.sevbanking.database.CustomerDTO;
import com.nw.sevbanking.database.GiroAccountDTO;
import com.nw.sevbanking.database.TransactionDTO;
import com.nw.sevbanking.exception.BalanceException;
import com.nw.sevbanking.exception.CreditcardHasAlreadyGiroAccountException;
import com.nw.sevbanking.exception.DispoLimitPassedException;
import com.nw.sevbanking.exception.IdNotFoundException;
import com.nw.sevbanking.exception.NegativeTransactionAmountException;
import com.nw.sevbanking.exception.PinIncorrectException;
import com.nw.sevbanking.repository.ICreditcardRepository;
import com.nw.sevbanking.repository.ICustomerRepository;
import com.nw.sevbanking.repository.IGiroAccountRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for Giro Accounts methods.
 * 
 * @author nicowickersheim
 *
 */

@RestController
@RequestMapping(path = "/api/v1/giroaccounts")
@RequiredArgsConstructor
public class V1GiroAccountController {

	@Autowired
	private IGiroAccountRepository giroAccountRepository;

	@Autowired
	private IGiroAccountRepository giroAccountToRepository;

	@Autowired
	private ICustomerRepository customerRepository;

	@Autowired
	private ICreditcardRepository creditcardRepository;

	/**
	 * Creates a new giro account
	 * 
	 * @param giroAccount
	 * @return Giro Account DTO
	 */
	@Operation(summary = "Create a giro account with random account id.")
	@PostMapping
	public GiroAccountDTO CreateGiroAccount(@RequestBody GiroAccountDTO giroAccount) {
		giroAccountRepository.save(giroAccount);
		return giroAccount;
	}

	/**
	 * Read a specific giro account
	 * 
	 * @param id
	 * @return Giro Account
	 * @throws IdNotFoundException - HTTP Status Code 404 ID not found
	 */
	@Operation(summary = "Read a specific giro account.")
	@GetMapping(value = "{id}")
	public GiroAccountDTO ReadGiroAccount(@PathVariable long id) throws IdNotFoundException {
		GiroAccountDTO giroAccount = giroAccountRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No giro account with the " + id + " found."));
		return giroAccount;

	}

	/**
	 * Updates a given giro account
	 * 
	 * @param id
	 * @param customer
	 * @return
	 * @throws IdNotFoundException   - HTTP Status Code 404 ID not found
	 * @throws PinIncorrectException
	 */
	@Operation(summary = "Update a specific giro account.")
	@PutMapping(value = "{id}")
	public GiroAccountDTO UpdateGiroAccount(@PathVariable long id, @RequestBody GiroAccountDTO giroAccount)
			throws IdNotFoundException, PinIncorrectException {
		GiroAccountDTO databaseGiroAccount = giroAccountRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No giro account with the id " + id + " found."));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!auth.getCredentials().equals(String.valueOf(databaseGiroAccount.getPin()))) {
			throw new PinIncorrectException("Pin not valid");
		}

		databaseGiroAccount.setAccountName(giroAccount.getAccountName());
		databaseGiroAccount.setPin(giroAccount.getPin());
		databaseGiroAccount.setDispoLimit(giroAccount.getDispoLimit());

		giroAccountRepository.save(databaseGiroAccount);
		return databaseGiroAccount;
	}

	/**
	 * Add a creditcard to giro account
	 * 
	 * @param id
	 * @param creditcardNumber
	 * @return
	 * @throws IdNotFoundException
	 * @throws CreditcardHasAlreadyGiroAccountException
	 * @throws PinIncorrectException
	 */
	@Operation(summary = "Add a creditcard to giro account.")
	@PutMapping(value = "{id}/creditcard/{creditcardNumber}")
	public CreditcardDTO AddCreditcardtoGiroAccount(@PathVariable long id, @PathVariable long creditcardNumber)
			throws IdNotFoundException, CreditcardHasAlreadyGiroAccountException, PinIncorrectException {
		CreditcardDTO databaseCreditcard = creditcardRepository.findById(creditcardNumber)
				.orElseThrow(() -> new IdNotFoundException("No creditard with the number " + id + " found."));
		GiroAccountDTO databaseGiroAccount = giroAccountRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No giro account with the id " + id + " found."));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!auth.getCredentials().equals(String.valueOf(databaseGiroAccount.getPin()))) {
			throw new PinIncorrectException("Pin not valid");
		}

		if (databaseCreditcard.getGiroAccount() == null) {
			databaseCreditcard.setGiroAccount(databaseGiroAccount);
		} else {
			throw new CreditcardHasAlreadyGiroAccountException("Creditcard already has a giro account.");
		}

		creditcardRepository.save(databaseCreditcard);

		return databaseCreditcard;

	}

	/**
	 * Add new transaction to giro account
	 * 
	 * @param id
	 * @param transaction
	 * @return
	 * @throws IdNotFoundException
	 * @throws PinIncorrectException
	 * @throws DispoLimitPassedException
	 */
	@Operation(summary = "Add new transaction to giro account.")
	@PostMapping(value = "{id}/transaction")
	public GiroAccountDTO AddTransaction(@PathVariable long id, @RequestBody AccountTransactionModel transaction)
			throws IdNotFoundException, PinIncorrectException, DispoLimitPassedException {
		GiroAccountDTO databaseGiroAccount = giroAccountRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No giro account with the id " + id + " found."));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		CustomerDTO customer = customerRepository.findById(Long.parseLong(auth.getName()))
				.orElseThrow(() -> new IdNotFoundException("Id not found."));
		if (!giroAccountRepository.existsByAccountIdAndCustomers_customerId(databaseGiroAccount.getAccountId(),
				customer.getCustomerId())) {
			throw new IdNotFoundException("No customer found.");
		}

		if (!auth.getCredentials().equals(String.valueOf(databaseGiroAccount.getPin()))) {
			throw new PinIncorrectException("Pin not valid.");
		}

		if ((databaseGiroAccount.getBalance() + transaction.getAmount()) < (databaseGiroAccount.getDispoLimit() * -1)) {
			throw new DispoLimitPassedException("Dispolimit passed.");
		}

		databaseGiroAccount.setBalance(databaseGiroAccount.getBalance() + transaction.getAmount());

		if (databaseGiroAccount.getTransaction() == null) {
			List<TransactionDTO> tempTransactions = new ArrayList<>();
			tempTransactions.add(new TransactionDTO(transaction.getAmount()));
			databaseGiroAccount.setTransaction(tempTransactions);
		} else {
			List<TransactionDTO> tempTransactions = databaseGiroAccount.getTransaction();
			tempTransactions.add(new TransactionDTO(transaction.getAmount()));
		}

		giroAccountRepository.save(databaseGiroAccount);

		return databaseGiroAccount;

	}

	/**
	 * Transfer amount from fromAccount to toAccount
	 * 
	 * @param id
	 * @param toAccountId
	 * @param transaction
	 * @return
	 * @throws IdNotFoundException
	 * @throws PinIncorrectException
	 * @throws DispoLimitPassedException
	 * @throws NegativeTransactionAmountException
	 * @throws BalanceException
	 */
	@Operation(summary = "Transfer an amount between two giro accounts.")
	@PostMapping(value = "{id}/transaction/{toAccountId}")
	public GiroAccountDTO AddTransactionFromAccountToAccount(@PathVariable long id, @PathVariable long toAccountId,
			@RequestBody AccountTransactionModel transaction) throws IdNotFoundException, PinIncorrectException,
			DispoLimitPassedException, NegativeTransactionAmountException, BalanceException {

		if (transaction.getAmount() <= 0) {
			throw new NegativeTransactionAmountException("Cannot transfer a zero or negative amount.");
		}

		GiroAccountDTO databaseFromGiroAccount = giroAccountRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No giro account with the id " + id + " found."));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		CustomerDTO customer = customerRepository.findById(Long.parseLong(auth.getName()))
				.orElseThrow(() -> new IdNotFoundException("Id not found."));
		if (!giroAccountRepository.existsByAccountIdAndCustomers_customerId(databaseFromGiroAccount.getAccountId(),
				customer.getCustomerId())) {
			throw new IdNotFoundException("No giro account with the id " + id + " found.");
		}

		if (!auth.getCredentials().equals(String.valueOf(databaseFromGiroAccount.getPin()))) {
			throw new PinIncorrectException("Pin not valid.");
		}

		if (databaseFromGiroAccount.getBalance() - transaction.getAmount() < 0) {
			throw new BalanceException("Balance to low.");
		}

		GiroAccountDTO databaseToGiroAccount = giroAccountToRepository.findById(toAccountId)
				.orElseThrow(() -> new IdNotFoundException("No giro account with the id " + id + " found."));

		if ((databaseFromGiroAccount.getBalance() + transaction.getAmount()) < (databaseFromGiroAccount.getDispoLimit()
				* -1)) {
			throw new DispoLimitPassedException("Dispolimit passed.");
		}

		databaseFromGiroAccount.setBalance(databaseFromGiroAccount.getBalance() - transaction.getAmount());

		if (databaseFromGiroAccount.getTransaction() == null) {
			List<TransactionDTO> tempTransactions = new ArrayList<>();
			tempTransactions.add(new TransactionDTO(transaction.getAmount()));
			databaseFromGiroAccount.setTransaction(tempTransactions);
		} else {
			List<TransactionDTO> tempTransactions = databaseFromGiroAccount.getTransaction();
			tempTransactions.add(new TransactionDTO(transaction.getAmount()));
		}

		giroAccountRepository.save(databaseFromGiroAccount);

		databaseToGiroAccount.setBalance(databaseToGiroAccount.getBalance() + transaction.getAmount());

		if (databaseToGiroAccount.getTransaction() == null) {
			List<TransactionDTO> tempTransactionsTo = new ArrayList<>();
			tempTransactionsTo.add(new TransactionDTO(transaction.getAmount()));
			databaseToGiroAccount.setTransaction(tempTransactionsTo);
		} else {
			List<TransactionDTO> tempTransactionsTo = databaseToGiroAccount.getTransaction();
			tempTransactionsTo.add(new TransactionDTO(transaction.getAmount()));
		}

		giroAccountRepository.save(databaseToGiroAccount);

		return databaseFromGiroAccount;

	}

}

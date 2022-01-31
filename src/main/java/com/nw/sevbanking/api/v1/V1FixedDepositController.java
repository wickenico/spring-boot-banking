package com.nw.sevbanking.api.v1;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import com.nw.sevbanking.database.FixedDepositAccountDTO;
import com.nw.sevbanking.database.TransactionDTO;
import com.nw.sevbanking.exception.DispoLimitPassedException;
import com.nw.sevbanking.exception.FixedDepositMonthLimitException;
import com.nw.sevbanking.exception.FixedDepositNegativeBalanceException;
import com.nw.sevbanking.exception.IdNotFoundException;
import com.nw.sevbanking.exception.InterestRateZeroException;
import com.nw.sevbanking.exception.PinIncorrectException;
import com.nw.sevbanking.repository.IFixedDepositAccountRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for Fixed Deposit Accounts
 * 
 * @author nicowickersheim
 *
 */

@RestController
@RequestMapping(path = "/api/v1/fixeddepositaccounts")
@RequiredArgsConstructor
public class V1FixedDepositController {

	@Autowired
	private IFixedDepositAccountRepository fixedAccountRepository;

	/**
	 * Creates a new Fixed Deposit Account
	 * 
	 * @param fixed Deposit account
	 * @return Fixed Deposit Account DTO
	 * @throws InterestRateZeroException
	 */
	@Operation(summary = "Create a fixed deposit account with random account id.")
	@PostMapping(consumes = { "application/json" })
	public FixedDepositAccountDTO CreateFixedDepositAccount(@RequestBody FixedDepositAccountDTO fixedDepositAccount)
			throws InterestRateZeroException {
		fixedDepositAccount.setDispoLimit(0);

		if (fixedDepositAccount.getInterestRate() <= 0) {
			throw new InterestRateZeroException("InterestRate must be greater zero.");
		}

		fixedAccountRepository.save(fixedDepositAccount);
		return fixedDepositAccount;
	}

	/**
	 * Read a specific Fixed Deposit Account
	 * 
	 * @param id
	 * @return Fixed Deposit Account
	 * @throws IdNotFoundException - HTTP Status Code 404 ID not found
	 */
	@Operation(summary = "Read a specific fixed deposit account.")
	@GetMapping(value = "{id}")
	public FixedDepositAccountDTO ReadFixedDepositAccount(@PathVariable long id) throws IdNotFoundException {
		FixedDepositAccountDTO databaseFixedDepositAccount = fixedAccountRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No fixed deposit account with the " + id + " found."));
		return databaseFixedDepositAccount;

	}

	/**
	 * Updates a given Fixed Deposit Account
	 * 
	 * @param id
	 * @param customer
	 * @return
	 * @throws IdNotFoundException   - HTTP Status Code 404 ID not found
	 * @throws PinIncorrectException
	 */
	@Operation(summary = "Update a specific fixed deposit account.")
	@PutMapping(value = "{id}")
	public FixedDepositAccountDTO UpdateFixedDepositAccount(@PathVariable long id,
			@RequestBody FixedDepositAccountDTO fixedDepositAccount) throws IdNotFoundException, PinIncorrectException {
		FixedDepositAccountDTO databaseFixedDepositAccount = fixedAccountRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No fixed deposit account with the id " + id + " found."));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!auth.getCredentials().equals(String.valueOf(databaseFixedDepositAccount.getPin()))) {
			throw new PinIncorrectException("Pin not valid");
		}

		databaseFixedDepositAccount.setAccountName(fixedDepositAccount.getAccountName());
		databaseFixedDepositAccount.setPin(fixedDepositAccount.getPin());
		databaseFixedDepositAccount.setDispoLimit(fixedDepositAccount.getDispoLimit());

		fixedAccountRepository.save(databaseFixedDepositAccount);
		return databaseFixedDepositAccount;
	}

	/**
	 * Add new transaction to Fixed Deposit Account
	 * 
	 * @param id
	 * @param transaction
	 * @return
	 * @throws IdNotFoundException
	 * @throws PinIncorrectException
	 * @throws DispoLimitPassedException
	 * @throws FixedDepositMonthLimitException
	 * @throws FixedDepositNegativeBalanceException
	 */
	
	@Operation(summary = "Add new transaction to fixed deposit account.")
	@PostMapping(value = "{id}/transaction")
	public FixedDepositAccountDTO AddTransaction(@PathVariable long id,
			@RequestBody AccountTransactionModel transaction) throws IdNotFoundException, PinIncorrectException,
			DispoLimitPassedException, FixedDepositMonthLimitException, FixedDepositNegativeBalanceException {
		FixedDepositAccountDTO databaseFixedDepositAccount = fixedAccountRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No fixed deposit account with the id " + id + " found."));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!auth.getCredentials().equals(String.valueOf(databaseFixedDepositAccount.getPin()))) {
			throw new PinIncorrectException("Pin not valid");
		}

		Date date = Date.from(ZonedDateTime.now().minusMonths(1).toInstant());

		List<TransactionDTO> transactionHistory = databaseFixedDepositAccount.getTransaction();
		if (transactionHistory == null && transaction.getAmount() <= 0) {
			throw new DispoLimitPassedException("No existing transactions yet. No amount can be withdrawn.");
		} else if (transactionHistory == null && transaction.getAmount() > 0) {
			List<TransactionDTO> tempTransactions = new ArrayList<>();
			tempTransactions.add(new TransactionDTO(transaction.getAmount()));
			databaseFixedDepositAccount.setTransaction(tempTransactions);
			databaseFixedDepositAccount.setBalance(transaction.getAmount());
		} else if (transactionHistory != null && transaction.getAmount() > 0) {
			transactionHistory.add(new TransactionDTO(transaction.getAmount()));
		} else {
			long actualUnavailableMoney = 0;
			for (int i = 0; i < transactionHistory.size(); i++) {
				TransactionDTO tempTransaction = transactionHistory.get(i);

				if (tempTransaction.getTransactionDate().compareTo(date) > 0)  {
					actualUnavailableMoney += tempTransaction.getAmount();
				}
			}
			
			if (actualUnavailableMoney <= 0) {
				throw new FixedDepositMonthLimitException("No balance older than one month.");
			}

			if ((databaseFixedDepositAccount.getBalance() - actualUnavailableMoney) < transaction.getAmount()) {
				throw new FixedDepositMonthLimitException("Balance too low.");
			}

			if (databaseFixedDepositAccount.getBalance() - transaction.getAmount() < 0) {
				throw new FixedDepositNegativeBalanceException("Account balance cannot go into the negative.");
			}

		}

		databaseFixedDepositAccount.setBalance(databaseFixedDepositAccount.getBalance() + transaction.getAmount());

		fixedAccountRepository.save(databaseFixedDepositAccount);

		return databaseFixedDepositAccount;

	}

}

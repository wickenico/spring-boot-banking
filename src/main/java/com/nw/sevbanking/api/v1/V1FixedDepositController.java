package com.nw.sevbanking.api.v1;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/fixeddepositaccounts")
@RequiredArgsConstructor
public class V1FixedDepositController {

	@Autowired
	private IFixedDepositAccountRepository fixedAccountRepository;

	/**
	 * Creates a new Fixed Deposit account
	 * 
	 * @param fixed Deposit account
	 * @return Fixed Deposit Account DTO
	 * @throws InterestRateZeroException 
	 */
	@PostMapping(consumes = { "application/json" })
	public FixedDepositAccountDTO CreateFixedDepositAccount(@RequestBody FixedDepositAccountDTO fixedDepositAccount) throws InterestRateZeroException {
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
	@GetMapping(value = "{id}")
	public FixedDepositAccountDTO ReadFixedDepositAccount(@PathVariable long id) throws IdNotFoundException {
		Optional<FixedDepositAccountDTO> databaseFixedDepositAccount = fixedAccountRepository.findById(id);
		if (databaseFixedDepositAccount.isPresent()) {
			return databaseFixedDepositAccount.get();
		}

		throw new IdNotFoundException("No giro account with the " + id + " found.");
	}

	/**
	 * Updates a given Fixed Deposit Account
	 * 
	 * @param id
	 * @param customer
	 * @return
	 * @throws IdNotFoundException - HTTP Status Code 404 ID not found
	 */
	@PutMapping(value = "{id}")
	public FixedDepositAccountDTO UpdateFixedDepositAccount(@PathVariable long id,
			@RequestBody FixedDepositAccountDTO fixedDepositAccount) throws IdNotFoundException {
		FixedDepositAccountDTO databaseFixedDepositAccount = fixedAccountRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No fixed deposit account with the id " + id + " found."));

		fixedAccountRepository.save(databaseFixedDepositAccount);
		return databaseFixedDepositAccount;
	}

	@PostMapping(value = "{id}/transaction")
	public FixedDepositAccountDTO AddTransaction(@PathVariable long id,
			@RequestBody AccountTransactionModel transaction) throws IdNotFoundException, PinIncorrectException,
			DispoLimitPassedException, FixedDepositMonthLimitException, FixedDepositNegativeBalanceException {
		FixedDepositAccountDTO databaseFixedDepositAccount = fixedAccountRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException("No fixed deposit account with the id " + id + " found."));

//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//		if (!auth.getCredentials().equals(String.valueOf(databaseFixedDepositAccount.getPin()))) {
//			throw new PinIncorrectException("Pin not valid");
//		}

		Date currentDate = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<TransactionDTO> transactionHistory = databaseFixedDepositAccount.getTransaction();
		if (transactionHistory == null && transaction.getAmount() <= 0) {
			throw new DispoLimitPassedException(
					"Noch keine existierenden Transaktionen. Es kann kein Geld abgehoben werden."); // TODO: Fixen
		} else if (transactionHistory == null && transaction.getAmount() > 0) {
			List<TransactionDTO> tempTransactions = new ArrayList<>();
			tempTransactions.add(new TransactionDTO(transaction.getAmount()));
			databaseFixedDepositAccount.setTransaction(tempTransactions);
			databaseFixedDepositAccount.setBalance(transaction.getAmount());
		} else if (transactionHistory != null && transaction.getAmount() > 0) {
			transactionHistory.add(new TransactionDTO(transaction.getAmount()));
			databaseFixedDepositAccount.setBalance(databaseFixedDepositAccount.getBalance() + transaction.getAmount());
		} else {
			long actualUnavailableMoney = 0;
			for (int i = 0; i < transactionHistory.size(); i++) {
				TransactionDTO tempTransaction = transactionHistory.get(i);

				if (tempTransaction.getTransactionDate().compareTo(currentDate) < 0) {
					actualUnavailableMoney += tempTransaction.getAmount();
				}
			}

			if (databaseFixedDepositAccount.getBalance() - actualUnavailableMoney > transaction.getAmount()) {
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

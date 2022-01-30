package com.nw.sevbanking.api.v1;

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
import com.nw.sevbanking.exception.IdNotFoundException;
import com.nw.sevbanking.exception.PinIncorrectException;
import com.nw.sevbanking.repository.IFixedDepositAccountRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="/api/v1/fixeddepositaccounts")
@RequiredArgsConstructor
public class V1FixedDepositController {

	@Autowired
	private IFixedDepositAccountRepository fixedAccountRepository;
	
	/**
	 * Creates a new Fixed Deposit account
	 * @param fixed Deposit account
	 * @return Fixed Deposit Account DTO
	 */
	@PostMapping(consumes= {"application/json"})
	public FixedDepositAccountDTO CreateFixedDepositAccount(@RequestBody FixedDepositAccountDTO fixedDepositAccount) {
		fixedDepositAccount.setBalance(0);
		fixedAccountRepository.save(fixedDepositAccount);
		return fixedDepositAccount;
	}
	
	/**
	 * Read a specific Fixed Deposit Account
	 * @param id
	 * @return Fixed Deposit Account
	 * @throws IdNotFoundException  - HTTP Status Code 404 ID not found
	 */
	@GetMapping(value= "{id}")
	public FixedDepositAccountDTO ReadGiroAccount(@PathVariable long id) throws IdNotFoundException {
		Optional<FixedDepositAccountDTO> databaseFixedDepositAccount = fixedAccountRepository.findById(id);
		if(databaseFixedDepositAccount.isPresent()) {
			return databaseFixedDepositAccount.get();
		}
		
		throw new IdNotFoundException("No giro account with the " + id + " found.");
	}
	
	/**
	 * Updates a given Fixed Deposit Account
	 * @param id
	 * @param customer
	 * @return
	 * @throws IdNotFoundException - HTTP Status Code 404 ID not found
	 */
	@PutMapping(value="{id}")
	public FixedDepositAccountDTO UpdateGiroAccount(@PathVariable long id, @RequestBody FixedDepositAccountDTO fixedDepositAccount) throws IdNotFoundException {
		Optional<FixedDepositAccountDTO> databaseFixedDepositAccount = fixedAccountRepository.findById(id);
		if(databaseFixedDepositAccount.isPresent()) {
			fixedAccountRepository.save(fixedDepositAccount);
		}
		throw new IdNotFoundException("No giro account with the id " + id + " found.");
	}
	
	@PostMapping(value="{id}/transaction")
	public FixedDepositAccountDTO AddTransaction(@PathVariable long id, @RequestBody AccountTransactionModel transaction) throws IdNotFoundException, PinIncorrectException, DispoLimitPassedException {
		Optional<FixedDepositAccountDTO> databaseFixedDepositAccount = fixedAccountRepository.findById(id);
		if(databaseFixedDepositAccount.isEmpty()) {
			throw new IdNotFoundException("No giro account with the id " + id + " found.");
		}
		
		var tempDepositAccount = databaseFixedDepositAccount.get();
		if(transaction.getPin()!=tempDepositAccount.getPin()) {
			throw new PinIncorrectException("Pin not valid");
		}
		
		
		Date currentDate=new Date();
		List<TransactionDTO> transactionHistory = tempDepositAccount.getTransaction();
		if(transactionHistory==null && transaction.getAmount()<=0) {
			throw new DispoLimitPassedException("Noch keine existierenden Transaktionen. Es kann kein Geld abgehoben werden."); //TODO: Fixen
		}else if(transactionHistory==null&&transaction.getAmount()>0) {
			List<TransactionDTO> tempTransactions = new ArrayList<>();
			tempTransactions.add(new TransactionDTO(transaction.getAmount()));
			tempDepositAccount.setTransaction(tempTransactions);
			tempDepositAccount.setBalance(transaction.getAmount());
		}else if(transactionHistory!=null&&transaction.getAmount()>0) {
			transactionHistory.add(new TransactionDTO(transaction.getAmount()));
			tempDepositAccount.setBalance(tempDepositAccount.getBalance()+transaction.getAmount());
		}else {
			long actualUnavailableMoney=0;
			for(int i = 0; i<transactionHistory.size(); i++) {
				//currentDate.
			}
		}
		
		
		tempDepositAccount.setBalance(tempDepositAccount.getBalance()+transaction.getAmount());
		
		
		fixedAccountRepository.save(tempDepositAccount);
		
		return tempDepositAccount;
		
	}
	
}

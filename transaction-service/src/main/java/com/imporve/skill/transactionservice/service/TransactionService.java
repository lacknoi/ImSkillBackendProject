package com.imporve.skill.transactionservice.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imporve.skill.transactionservice.AppConstant;
import com.imporve.skill.transactionservice.dto.AccountBalanceRequest;
import com.imporve.skill.transactionservice.dto.TransactionRequest;
import com.imporve.skill.transactionservice.dto.TransactionResponse;
import com.imporve.skill.transactionservice.model.AccountBalance;
import com.imporve.skill.transactionservice.model.AccountBalanceTransaction;
import com.imporve.skill.transactionservice.repository.AccountBalanceRepository;
import com.imporve.skill.transactionservice.repository.AccountBalanceTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {
	private final AccountBalanceRepository accountBalanceRepository;
	private final AccountBalanceTransactionRepository transactionRepository;
	
	public void initAccountBalance(AccountBalanceRequest accountBalanceRequest) {
		AccountBalance accountBalance = new AccountBalance();
		accountBalance.setAccountNo(accountBalanceRequest.getAccountNo());
		accountBalance.setTotalBalance(BigDecimal.ZERO);
		accountBalance.setCreated(new Date());
		accountBalance.setCreatedBy("INIT");
		accountBalance.setLastUpd(new Date());
		accountBalance.setLastUpdBy("INIT");
		
		accountBalanceRepository.save(accountBalance);
	}
	
	private String generateTransactionNo() {
		String format = "T";
		Long accountNoSeq = transactionRepository.getNextValOrderNoSequence();
		
		return format + String.format("%04d", accountNoSeq);
	}
	
	private AccountBalanceTransaction mapTransactionRequestToTransaction
										(TransactionRequest transactionRequest
												, String type
												, BigDecimal total) {
		
		
		AccountBalanceTransaction transaction = new AccountBalanceTransaction();
		transaction.setAccountNo(transactionRequest.getAccountNo());
		transaction.setActionType(type);
		transaction.setTransactionNo(generateTransactionNo());
		transaction.setRequestAmount(transactionRequest.getAmount());
		transaction.setTotalBalance(total);
		transaction.setCreated(new Date());
		transaction.setCreatedBy(transactionRequest.getUserName());
		transaction.setLastUpd(new Date());
		transaction.setLastUpdBy(transactionRequest.getUserName());
		
		return transaction;
	}
	
	public TransactionResponse deposit(TransactionRequest transactionRequest) {
		AccountBalance accountBalance = accountBalanceRepository.findByAccountNo(transactionRequest.getAccountNo());
		
		accountBalance.setTotalBalance(accountBalance.getTotalBalance().add(transactionRequest.getAmount()));
		
		AccountBalanceTransaction transaction = mapTransactionRequestToTransaction(transactionRequest, AppConstant.TRANSACTION_DEPOSIT, accountBalance.getTotalBalance());
	
		accountBalance.setLastUpd(new Date());
		accountBalance.setLastUpdBy(transactionRequest.getUserName());
		
		accountBalanceRepository.save(accountBalance);
		transactionRepository.save(transaction);
		
		return TransactionResponse.builder()
					.transactionNo(transaction.getTransactionNo()).build();
	}
	
	public TransactionResponse withdraw(TransactionRequest transactionRequest) {
		TransactionResponse response = new TransactionResponse();
		
		AccountBalance accountBalance = accountBalanceRepository.findByAccountNo(transactionRequest.getAccountNo());
		
		
		if(accountBalance.getTotalBalance().compareTo(transactionRequest.getAmount()) > 0) {
			accountBalance.setTotalBalance(accountBalance.getTotalBalance().subtract(transactionRequest.getAmount()));
			
			AccountBalanceTransaction transaction = mapTransactionRequestToTransaction(transactionRequest, AppConstant.TRANSACTION_WITHDRAW, accountBalance.getTotalBalance());
		
			accountBalance.setLastUpd(new Date());
			accountBalance.setLastUpdBy(transactionRequest.getUserName());
			
			accountBalanceRepository.save(accountBalance);
			transactionRepository.save(transaction);
			
			response.setTransactionNo(transaction.getTransactionNo());
		}else {
			response.setErrorCode("E001");
			response.setErrorDescription("Balance is not enough.");
		}
		
		return response;
	}
}

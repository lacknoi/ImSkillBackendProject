package com.imporve.skill.transactionservice.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.imporve.skill.transactionservice.AppConstant;
import com.imporve.skill.transactionservice.dto.AccountBalanceRequest;
import com.imporve.skill.transactionservice.dto.AccountResponse;
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
	private final WebClient.Builder webClientBuilder;
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
	
	public TransactionResponse transfer(TransactionRequest transactionRequest) {
		TransactionResponse response = new TransactionResponse();
		
		AccountResponse accountResponse = webClientBuilder.build().get()
				.uri("http://account-service/api/account/accountno",
						uriBuilder -> uriBuilder.queryParam("account-no", transactionRequest.getDestAccountNo()).build())
				.retrieve()
				.bodyToMono(AccountResponse.class)
				.block();
		
		if(!StringUtils.isEmpty(accountResponse.getAccntNo())) {
			AccountBalance fromAcc  = accountBalanceRepository.findByAccountNo(transactionRequest.getAccountNo());
			AccountBalance destAcc = accountBalanceRepository.findByAccountNo(transactionRequest.getAccountNo());
			
			fromAcc.setTotalBalance(fromAcc.getTotalBalance().subtract(transactionRequest.getAmount()));
			destAcc.setTotalBalance(destAcc.getTotalBalance().add(transactionRequest.getAmount()));
			
			AccountBalanceTransaction fromTransaction = mapTransactionRequestToTransaction(transactionRequest
										, AppConstant.TRANSACTION_DEPOSIT, fromAcc.getTotalBalance());
			AccountBalanceTransaction destTransaction = mapTransactionRequestToTransaction(transactionRequest
										, AppConstant.TRANSACTION_DEPOSIT, destAcc.getTotalBalance());
			
			fromAcc.setLastUpd(new Date());
			fromAcc.setLastUpdBy(transactionRequest.getUserName());
			destAcc.setLastUpd(new Date());
			destAcc.setLastUpdBy(transactionRequest.getUserName());
			
			accountBalanceRepository.save(fromAcc);
			accountBalanceRepository.save(destAcc);
			transactionRepository.save(fromTransaction);
			transactionRepository.save(destTransaction);
		}else {
			response.setErrorCode(AppConstant.ERR_CODE_DEST_NOT_FOUND);
			response.setErrorDescription(AppConstant.ERR_MSG_DEST_NOT_FOUND);
		}
		
		return response;
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
			response.setErrorCode(AppConstant.ERR_CODE_BALANCE_NOT_ENOUGH);
			response.setErrorDescription(AppConstant.ERR_MSG_BALANCE_NOT_ENOUGH);
		}
		
		return response;
	}
}

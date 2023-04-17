package com.imporve.skill.transactionservice.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imporve.skill.transactionservice.dto.AccountBalanceRequest;
import com.imporve.skill.transactionservice.dto.DepositRequest;
import com.imporve.skill.transactionservice.model.AccountBalance;
import com.imporve.skill.transactionservice.repository.AccountBalanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {
	private final AccountBalanceRepository accountBalanceRepository;
	
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
	
	public void deposit(DepositRequest depositRequest) {
		
	}
}

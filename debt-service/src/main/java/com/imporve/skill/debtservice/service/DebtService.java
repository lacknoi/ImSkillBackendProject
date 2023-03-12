package com.imporve.skill.debtservice.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imporve.skill.debtservice.dto.DebtRequest;
import com.imporve.skill.debtservice.model.AccountBalance;
import com.imporve.skill.debtservice.repository.AccountBalanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DebtService {
	private final AccountBalanceRepository accountBalanceRepository;
	
	public void initAccountBalance(DebtRequest debtRequest) {
		AccountBalance accountBalance = new AccountBalance();
		accountBalance.setBaNo(debtRequest.getBaNo());
		accountBalance.setInvoiceTotalBalance(BigDecimal.ZERO);
		accountBalance.setCreated(new Date());
		accountBalance.setCreatedBy(debtRequest.getTransactionBy());
		accountBalance.setLastUpd(new Date());
		accountBalance.setLastUpdBy(debtRequest.getTransactionBy());
		
		accountBalanceRepository.save(accountBalance);
	}
}

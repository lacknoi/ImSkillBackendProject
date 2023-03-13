package com.imporve.skill.debtservice.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imporve.skill.debtservice.dto.DebtRequest;
import com.imporve.skill.debtservice.model.AccountBalance;
import com.imporve.skill.debtservice.model.Blacklist;
import com.imporve.skill.debtservice.repository.AccountBalanceRepository;
import com.imporve.skill.debtservice.repository.BlacklistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DebtService {
	private final AccountBalanceRepository accountBalanceRepository;
	private final BlacklistRepository blacklistRepository;
	
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
	
	public boolean checkBlackListStatus(String boNa) {
		Blacklist blacklist = blacklistRepository.findByBaNo(boNa);
		
		return blacklist != null && StringUtils.equals(blacklist.getStatusCd(), "Active");
	}
}

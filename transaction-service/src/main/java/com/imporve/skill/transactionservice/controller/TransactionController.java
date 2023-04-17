package com.imporve.skill.transactionservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.imporve.skill.transactionservice.dto.AccountBalanceRequest;
import com.imporve.skill.transactionservice.dto.DepositRequest;
import com.imporve.skill.transactionservice.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
	private final TransactionService transactionService;
	
	@PostMapping("/init-account-balance")
	@ResponseStatus(HttpStatus.CREATED)
	public void initAccountBalance(@RequestBody AccountBalanceRequest debtRequest) {
		transactionService.initAccountBalance(debtRequest);
	}
	
	@PostMapping("/deposit")
	public ResponseEntity<String> deposit(@RequestBody DepositRequest depositRequest){
		transactionService.deposit(depositRequest);
		
		return new ResponseEntity<>("deposit", HttpStatus.OK);
	}
}

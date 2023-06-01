package com.imporve.skill.transactionservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.imporve.skill.transactionservice.dto.AccountBalanceRequest;
import com.imporve.skill.transactionservice.dto.TransactionDescriptionResponse;
import com.imporve.skill.transactionservice.dto.TransactionRequest;
import com.imporve.skill.transactionservice.dto.TransactionResponse;
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
	public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionRequest transactionRequest){
		TransactionResponse response = transactionService.deposit(transactionRequest);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<TransactionResponse> withdraw(@RequestBody TransactionRequest transactionRequest){
		TransactionResponse response = transactionService.withdraw(transactionRequest);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<TransactionResponse> transfer(@RequestBody TransactionRequest transactionRequest){
		TransactionResponse response = transactionService.transfer(transactionRequest);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/transactions")
	public ResponseEntity<TransactionDescriptionResponse> transaction(@RequestParam String accountNo){
		TransactionDescriptionResponse response = new TransactionDescriptionResponse();
		
		System.out.println("accountNo : " + accountNo);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}

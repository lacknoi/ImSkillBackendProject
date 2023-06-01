package com.imporve.skill.accountservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.imporve.skill.accountservice.dto.AccountFileWrapperRequest;
import com.imporve.skill.accountservice.dto.AccountRequest;
import com.imporve.skill.accountservice.dto.AccountResponse;
import com.imporve.skill.accountservice.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;
	
	@GetMapping("/{account-no}")
	@ResponseStatus(HttpStatus.OK)
	public AccountResponse getAccountByNo(@PathVariable("account-no") String accountNo) {
		return accountService.getAccountByAccountNo(accountNo);
	}
	
	@GetMapping("/accountno")
	@ResponseStatus(HttpStatus.OK)
	public AccountResponse getAccountByAccNo(@RequestParam("account-no") String accountNo) {
		System.out.println("accountNo : " + accountNo);
		
		return accountService.getAccountByAccountNo(accountNo);
	}
	
//	@GetMapping
//	@ResponseStatus(HttpStatus.OK)
//	public List<AccountResponse> getAccountByBANo(@RequestParam List<String> baNo) {
//        return accountService.getAccountByNo(baNo);
//	}
	
//	@GetMapping
//	@ResponseStatus(HttpStatus.OK)
//	public AccountResponse getAccountByBANo(@RequestParam("account-level") String accountLevel
//								, @RequestParam("account-no") String accountNo) {
//        return accountService.getAccountByaccountLevelAndAccountNo(accountLevel, accountNo);
//	}
	
	@GetMapping("/billing/cano")
	@ResponseStatus(HttpStatus.OK)
	public List<AccountResponse> getAccountByMasterNo(@RequestParam("account-no") String accountNo) {
        return accountService.getAccountByMasterNo(accountNo);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public List<AccountResponse> createAccounts(@RequestBody List<AccountRequest> accountRequests) {
        return accountService.createAccounts(accountRequests);
	}
	
	@PostMapping("/create-account")
	public ResponseEntity<AccountResponse> createAccount(AccountRequest accountRequest){
		AccountResponse accountResponse = accountService.createAccount(accountRequest);
		
		return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
	}
	
	@PostMapping("/create-account-upload-file")
	public ResponseEntity<AccountResponse> createAccountFileWrapper(AccountFileWrapperRequest accountFileWrapperRequest) throws IOException{
		AccountResponse accountResponse = accountService.createAccountFileWrapper(accountFileWrapperRequest);
		
		return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
	}
}

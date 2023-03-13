package com.imporve.skill.accountservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.imporve.skill.accountservice.dto.AccountRequest;
import com.imporve.skill.accountservice.dto.AccountResponse;
import com.imporve.skill.accountservice.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;
	
	@GetMapping("/accounts")
	public String listAll() {
        System.out.println("listAll"); 
		 
        return "accounts";
	}

	@GetMapping("/billing/cano/{ca-no}")
	@ResponseStatus(HttpStatus.OK)
	public AccountResponse getAccountByCANo(@PathVariable("ca-no") String caNo) {
		return accountService.getBAInfoByCaNo(caNo);
	}
	
//	@GetMapping
//	@ResponseStatus(HttpStatus.OK)
//	public List<AccountResponse> getAccountByBANo(@RequestParam List<String> baNo) {
//        return accountService.getAccountByNo(baNo);
//	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public AccountResponse getAccountByBANo(@RequestParam("account-level") String accountLevel
								, @RequestParam("account-no") String accountNo) {
        return accountService.getAccountByaccountLevelAndAccountNo(accountLevel, accountNo);
	}
	
	@GetMapping("/billing/cano")
	@ResponseStatus(HttpStatus.OK)
	public List<AccountResponse> getAccountByMasterNo(@RequestParam("account-no") String accountNo) {
        return accountService.getAccountByMasterNo(accountNo);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public List<AccountResponse> createAccount(@RequestBody List<AccountRequest> accountRequests) {
        return accountService.createAccount(accountRequests);
	}
}

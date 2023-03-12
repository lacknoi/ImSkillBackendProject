package com.imporve.skill.debtservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.imporve.skill.debtservice.dto.DebtRequest;
import com.imporve.skill.debtservice.service.DebtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/debt")
@RequiredArgsConstructor
public class DebtController {
	private final DebtService debtService;
	
	@PostMapping("/init-account-balance")
	@ResponseStatus(HttpStatus.CREATED)
	public void initAccountBalance(@RequestBody DebtRequest debtRequest) {
		System.out.println("--initAccountBalance--");
		
		debtService.initAccountBalance(debtRequest);
	}
}

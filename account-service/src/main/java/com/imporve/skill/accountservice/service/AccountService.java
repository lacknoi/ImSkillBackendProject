package com.imporve.skill.accountservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imporve.skill.accountservice.dto.AccountResponse;
import com.imporve.skill.accountservice.model.BAInfo;
import com.imporve.skill.accountservice.repository.BAInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final BAInfoRepository baInfoRepository;
	
	@Transactional(readOnly = true)
	public void getAccountBaNo(String accntNo) {
		BAInfo baInfo = baInfoRepository.findByBaNo(accntNo);
		
		System.out.println(baInfo.getBaName());
	}
	
	@Transactional(readOnly = true)
	public AccountResponse getBAInfoByCaNo(String accntNo) {
		BAInfo baInfo = baInfoRepository.findByCaNo(accntNo);
		
		AccountResponse accountResponse = AccountResponse.builder()
				.accntNo(baInfo.getBaNo())
				.accntName(baInfo.getBaName())
				.build();
		
		return accountResponse;
	}
	
	public List<AccountResponse> getAccountByNo(List<String> baNo) {
		return baInfoRepository.findByBaNoIn(baNo).stream()
			.map(baInfo ->
					AccountResponse.builder()
					.accntNo(baInfo.getBaNo())
					.accntName(baInfo.getBaName()).build()
				).toList();
	}
}

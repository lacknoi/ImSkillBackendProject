package com.imporve.skill.accountservice.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.imporve.skill.accountservice.dto.AccountRequest;
import com.imporve.skill.accountservice.dto.AccountResponse;
import com.imporve.skill.accountservice.dto.DebtRequest;
import com.imporve.skill.accountservice.model.Account;
import com.imporve.skill.accountservice.model.BAInfo;
import com.imporve.skill.accountservice.repository.AccountRepository;
import com.imporve.skill.accountservice.repository.BAInfoRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final BAInfoRepository baInfoRepository;
	private final AccountRepository accountRepository;
	private final WebClient.Builder webClientBuilder;
	
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
	
	public List<AccountResponse> createAccount(List<AccountRequest> accountRequestList) {
		List<Account> accountList = accountRequestList
						                .stream()
						                .map(accountRequest -> mapAccountRequestToAccount(accountRequest))
						                .toList();
		
		accountList = accountRepository.saveAll(accountList);
		
		List<AccountResponse> accountResponseList = accountList.stream()
												.map(account ->
														AccountResponse.builder()
															.accountId(account.getAccountId()).build()
													).toList();
		
		for(Account account : accountList) {
			if(StringUtils.equalsIgnoreCase(account.getAccountLevel(), "BA")) {
				System.out.println(" -- > " + account.getAccountNo());
				
				DebtRequest debtRequest = DebtRequest.builder()
						.baNo(account.getAccountNo())
						.transactionBy(null)
						.build();
				
				webClientBuilder.build().post()
						.uri("http://debt-service/api/debt/init-account-balance")
						.body(Mono.just(debtRequest), DebtRequest.class)
						.retrieve()
						.bodyToMono(String.class)
						.block();
			}
		}
		
		return accountResponseList;
	}
	
	private Account mapAccountRequestToAccount(AccountRequest accountRequest) {
		Account account = new Account();
		account.setAccountNo("testAccountNo");
		account.setAccountName(accountRequest.getAccountName());
		account.setAccountLevel(accountRequest.getAccountLevel());
		account.setCreated(new Date());
		account.setCreatedBy(accountRequest.getTransactionBy());
		account.setLastUpd(new Date());
		account.setLastUpdBy(accountRequest.getTransactionBy());
		
        return account;
    }
}

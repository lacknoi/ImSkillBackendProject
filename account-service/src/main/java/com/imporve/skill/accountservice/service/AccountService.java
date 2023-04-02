package com.imporve.skill.accountservice.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.imporve.skill.accountservice.AppConstant;
import com.imporve.skill.accountservice.dto.AccountFileWrapperRequest;
import com.imporve.skill.accountservice.dto.AccountRequest;
import com.imporve.skill.accountservice.dto.AccountResponse;
import com.imporve.skill.accountservice.dto.DebtRequest;
import com.imporve.skill.accountservice.model.Account;
import com.imporve.skill.accountservice.model.BAInfo;
import com.imporve.skill.accountservice.repository.AccountRepository;
import com.imporve.skill.accountservice.repository.BAInfoRepository;
import com.imporve.skill.accountservice.utils.DateTimeUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
	private final BAInfoRepository baInfoRepository;
	private final AccountRepository accountRepository;
	private final WebClient.Builder webClientBuilder;
	
	public void getAccountBaNo(String accntNo) {
		BAInfo baInfo = baInfoRepository.findByBaNo(accntNo);
		
		System.out.println(baInfo.getBaName());
	}
	
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
	
	public AccountResponse getAccountByAccountNo(String accntNo) {
		Account account = accountRepository.findByAccountNo(accntNo);
		
		AccountResponse accountResponse = AccountResponse.builder()
				.accntNo(account.getAccountNo())
				.accntName(account.getAccountName())
				.build();
		
		return accountResponse;
	}
	
	public AccountResponse getAccountByaccountLevelAndAccountNo(String accountLevel, String accountNo) {
		Account account = accountRepository.findByAccountLevelAndAccountNo(accountLevel, accountNo);
		
		return AccountResponse.builder()
				.accntName(account.getAccountName())
				.accntNo(account.getAccountNo()).build();
	}
	
	public List<AccountResponse> getAccountByMasterNo(String accountNo) {
		return accountRepository.findByMasterNo(accountNo).stream()
			.map(account -> mapAccountToAccountResponse(account)).toList();
	}
	
	public AccountResponse createAccountFileWrapper(AccountFileWrapperRequest accountFileWrapperRequest) {
		Account account = mapAccountRequestToAccount(accountFileWrapperRequest);
		
		account = accountRepository.save(account);
		
		AccountResponse accountResponse = mapAccountToAccountResponse(account);
		
		return accountResponse;
	}
	
	public List<AccountResponse> createAccounts(List<AccountRequest> accountRequestList) {
		List<Account> accountList = accountRequestList
						                .stream()
						                .map(accountRequest -> mapAccountRequestToAccount(accountRequest))
						                .toList();
		
		accountList = accountRepository.saveAll(accountList);
		
		List<AccountResponse> accountResponseList = accountList.stream()
												.map(account -> mapAccountToAccountResponse(account))
												.toList();
		
		for(Account account : accountList) {
			if(StringUtils.equalsIgnoreCase(account.getAccountLevel(), "BA")) {
				System.out.println(" -- > " + account.getAccountNo());
				
				DebtRequest debtRequest = DebtRequest.builder()
						.baNo(account.getAccountNo())
						.transactionBy(account.getLastUpdBy())
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
	
	private String generateAccountNo() {
		String dateFor = DateTimeUtils.formatDate(AppConstant.ACCOUNT_NO_FORMAT, DateTimeUtils.currentDate());
		Long accountNoSeq = accountRepository.getNextValAccountNoSequence(dateFor + "%");
		
		return dateFor + String.format("%04d", accountNoSeq);
	}
	
	private Account mapAccountRequestToAccount(AccountRequest accountRequest) {
		Account account = new Account();
		account.setAccountNo(generateAccountNo());
		account.setAccountName(accountRequest.getAccountName());
		account.setAccountLevel(accountRequest.getAccountLevel());
		account.setCreated(new Date());
		account.setCreatedBy(accountRequest.getTransactionBy());
		account.setLastUpd(new Date());
		account.setLastUpdBy(accountRequest.getTransactionBy());
		
        return account;
    }
	
	private AccountResponse mapAccountToAccountResponse(Account account) {
		return AccountResponse.builder()
		.accountId(account.getAccountId()).build();
	}
}

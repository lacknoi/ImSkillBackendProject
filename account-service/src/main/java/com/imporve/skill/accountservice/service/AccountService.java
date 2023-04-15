package com.imporve.skill.accountservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.imporve.skill.accountservice.AppConstant;
import com.imporve.skill.accountservice.dto.AccountFileWrapperRequest;
import com.imporve.skill.accountservice.dto.AccountRequest;
import com.imporve.skill.accountservice.dto.AccountResponse;
import com.imporve.skill.accountservice.model.Account;
import com.imporve.skill.accountservice.model.AccountAttachFile;
import com.imporve.skill.accountservice.model.BAInfo;
import com.imporve.skill.accountservice.repository.AccountRepository;
import com.imporve.skill.accountservice.repository.BAInfoRepository;
import com.imporve.skill.accountservice.utils.DateTimeUtils;

import lombok.RequiredArgsConstructor;

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
	
	private AccountResponse generateAccount(AccountRequest accountRequest) {
		Account account = mapAccountRequestToAccount(accountRequest);
		
		account = accountRepository.save(account);
		
//		if(StringUtils.equalsIgnoreCase(account.getAccountLevel(), "BA")) {
//			DebtRequest debtRequest = DebtRequest.builder()
//					.baNo(account.getAccountNo())
//					.transactionBy(account.getLastUpdBy())
//					.build();
//			
//			webClientBuilder.build().post()
//					.uri("http://debt-service/api/debt/init-account-balance")
//					.body(Mono.just(debtRequest), DebtRequest.class)
//					.retrieve()
//					.bodyToMono(String.class)
//					.block();
//		}
		
		AccountResponse accountResponse = mapAccountToAccountResponse(account);
		
		return accountResponse;
	}
	
	public AccountResponse createAccount(AccountRequest accountRequest) {
		AccountResponse accountResponse = generateAccount(accountRequest);
		
		return accountResponse;
	}
	
	public AccountResponse createAccountFileWrapper(AccountFileWrapperRequest accountFileWrapperRequest) {
		AccountResponse accountResponse = generateAccount(accountFileWrapperRequest);
		
		return accountResponse;
	}
	
	public List<AccountResponse> createAccounts(List<AccountRequest> accountRequestList) {
		List<AccountResponse> accountResponseList = new ArrayList<>();
		for(AccountRequest accountRequest : accountRequestList) {
			AccountResponse accountResponse = generateAccount(accountRequest);
			
			accountResponseList.add(accountResponse);
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
		
		if(accountRequest instanceof AccountFileWrapperRequest) {
			AccountFileWrapperRequest accountFileWrapperRequest = (AccountFileWrapperRequest) accountRequest;
			
			if(accountFileWrapperRequest.getFile() != null) {
				account.setAccountAttachFiles(new ArrayList<>());
				
				MultipartFile multipartFile = accountFileWrapperRequest.getFile();
				
				AccountAttachFile accountAttachFile = new AccountAttachFile();
				accountAttachFile.setAccount(account);
				accountAttachFile.setFileName(multipartFile.getOriginalFilename());
				accountAttachFile.setFileType(multipartFile.getContentType());
				accountAttachFile.setCreated(new Date());
				accountAttachFile.setCreatedBy(accountRequest.getTransactionBy());
				accountAttachFile.setLastUpd(new Date());
				accountAttachFile.setLastUpdBy(accountRequest.getTransactionBy());
				
				account.getAccountAttachFiles().add(accountAttachFile);
			}
		}
		
        return account;
    }
	
	private AccountResponse mapAccountToAccountResponse(Account account) {
		return AccountResponse.builder()
		.accountId(account.getAccountId()).build();
	}
}

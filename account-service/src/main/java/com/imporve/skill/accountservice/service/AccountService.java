package com.imporve.skill.accountservice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.imporve.skill.accountservice.AppConstant;
import com.imporve.skill.accountservice.dto.AccountBalanceRequest;
import com.imporve.skill.accountservice.dto.AccountFileWrapperRequest;
import com.imporve.skill.accountservice.dto.AccountRequest;
import com.imporve.skill.accountservice.dto.AccountResponse;
import com.imporve.skill.accountservice.model.Account;
import com.imporve.skill.accountservice.model.AccountAttachFile;
import com.imporve.skill.accountservice.repository.AccountRepository;
import com.imporve.skill.accountservice.repository.ConfigRepository;
import com.imporve.skill.accountservice.utils.DateTimeUtils;
import com.imporve.skill.accountservice.utils.FileUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
	private final WebClient.Builder webClientBuilder;
	private final AccountRepository accountRepository;
	private final ConfigRepository configRepository;
	
	public AccountResponse getAccountByAccountNo(String accntNo) {
		Account account = accountRepository.findByAccountNo(accntNo);
		
		AccountResponse accountResponse = AccountResponse.builder()
				.accntNo(account.getAccountNo())
				.accntName(account.getAccountName())
				.build();
		
		return accountResponse;
	}
	
	public List<AccountResponse> getAccountByMasterNo(String accountNo) {
		return accountRepository.findByMasterNo(accountNo).stream()
			.map(account -> mapAccountToAccountResponse(account)).toList();
	}
	
	private AccountResponse generateAccount(AccountRequest accountRequest) {
		Account account = mapAccountRequestToAccount(accountRequest);
		
		account = accountRepository.save(account);
		
		AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.builder()
					.accountNo(account.getAccountNo())
					.build();
			
		ResponseEntity<String> response = webClientBuilder.build().post()
				.uri("http://transaction-service/api/transaction/init-account-balance")
				.body(Mono.just(accountBalanceRequest), AccountBalanceRequest.class)
				.retrieve()
				.toEntity(String.class)
				.block();
		
		AccountResponse accountResponse = mapAccountToAccountResponse(account);
		
		return accountResponse;
	}
	
	public AccountResponse createAccount(AccountRequest accountRequest) {
		AccountResponse accountResponse = generateAccount(accountRequest);
		
		return accountResponse;
	}
	
	public AccountResponse createAccountFileWrapper(AccountFileWrapperRequest accountFileWrapperRequest) throws IOException {
		AccountResponse accountResponse = generateAccount(accountFileWrapperRequest);
		
		if(accountFileWrapperRequest.getFile() != null) {
			String path = configRepository.getPath("ORDER_ATTACH_FILE");
			String fileName = accountResponse.getAccntNo() + "_" + accountFileWrapperRequest.getFile().getOriginalFilename();
			
			FileUtils.saveFile(fileName, path, accountFileWrapperRequest.getFile());
		}
		
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

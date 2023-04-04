package com.imporve.skill.accountservice.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TEST_ACCOUNT", schema = "debt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;
    private String masterId;
	private String accountNo;
	private String accountName;
	private String accountLevel;
	private Date created;
	private String createdBy;
	private Date lastUpd;
	private String lastUpdBy;
	
	//Join
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<AccountAttachFile> accountAttachFiles;
}

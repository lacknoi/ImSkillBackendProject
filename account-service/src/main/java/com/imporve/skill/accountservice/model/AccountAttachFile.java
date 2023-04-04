package com.imporve.skill.accountservice.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "debt", name = "test_account_attach_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class AccountAttachFile{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accountAttachFileId;
	@ManyToOne
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;
	private String fileName;
	private String fileType;
	private Date created;
	private String createdBy;
	private Date lastUpd;
	private String lastUpdBy;
}

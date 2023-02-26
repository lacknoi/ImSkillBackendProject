package com.imporve.skill.accountservice.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CL_BA_INFO", schema = "debt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BAInfo {
	@Id
	@Column(name = "BA_NO")
	private String baNo;
	@Column(name = "CA_NO")
	private String caNo;
	@Column(name = "BA_NAME")
	private String baName;
}

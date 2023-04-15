package com.imporve.skill.orderservice.model;

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
@Table(name = "TEST_ORDER", schema = "debt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
	private String orderNo;
	private String orderType;
	private String statusCd;
	private String accountName;
	private Date created;
	private String createdBy;
	private Date lastUpd;
	private String lastUpdBy;
	
	@OneToMany(mappedBy="order" , cascade = CascadeType.ALL)
    private List<OrderAttachFile> items;
}

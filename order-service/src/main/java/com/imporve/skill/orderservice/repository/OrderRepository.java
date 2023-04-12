package com.imporve.skill.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.imporve.skill.orderservice.model.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {
	Order findByOrderNo(String orderNo);
    void deleteByOrderNo(String orderNo);
    
    @Query(value = "SELECT ISNULL(max(RIGHT(orderNo, 4)) + 1, 1) accountNo from Order where orderNo like :orderNo")
	Long getNextValOrderNoSequence(String orderNo);
}

package com.imporve.skill.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.imporve.skill.orderservice.model.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {
	Order findByOrderNo(String orderNo);
    void deleteByOrderNo(String orderNo);
}

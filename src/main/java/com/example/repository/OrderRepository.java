package com.example.repository;

import com.example.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by marek.papis on 2016-06-08.
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {
}

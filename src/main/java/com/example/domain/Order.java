package com.example.domain;

import javax.persistence.*;

/**
 * Created by marek.papis on 2016-06-08.
 */
@Entity
@Table(name = "orderEntity")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String orderName;

    public Order() {
    }

    public Order(String orderDescription) {
        this.orderName = orderDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Order))
            return false;

        Order order = (Order) o;

        if (id != order.id)
            return false;
        return orderName != null ? orderName.equals(order.orderName) : order.orderName == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (orderName != null ? orderName.hashCode() : 0);
        return result;
    }

    public int getId() {
        return id;
    }


    public String getOrderName() {
        return orderName;
    }

}

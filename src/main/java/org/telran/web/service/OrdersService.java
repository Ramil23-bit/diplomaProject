package org.telran.web.service;

import org.telran.web.entity.Orders;

import java.util.List;

public interface OrdersService {

    Orders create(Orders orders);
    List<Orders> checkOrderStatus();
    List<Orders> getAll();
    Orders getById(Long id);

}

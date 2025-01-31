package org.telran.web.service;

import org.telran.web.entity.OrderItems;

import java.util.Collection;

public interface OrderItemsService {

    OrderItems createOrderItems(OrderItems entity);

    Collection<OrderItems> getAllOrderItems();

    OrderItems getByIdOrderItems(Long id);

    void deleteOrderItems(Long id);
}

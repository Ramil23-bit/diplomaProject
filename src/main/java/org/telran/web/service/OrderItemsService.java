package org.telran.web.service;

import org.telran.web.entity.OrderItems;

import java.util.Collection;
import java.util.List;

public interface OrderItemsService {

    OrderItems createOrderItems(OrderItems orderItems);

    List<OrderItems> getAllOrderItems();

    OrderItems getByIdOrderItems(Long id);
}

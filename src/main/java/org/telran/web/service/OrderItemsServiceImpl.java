package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.OrderItems;
import org.telran.web.exception.CartNotFoundException;
import org.telran.web.exception.OrderItemsNotFoundException;
import org.telran.web.repository.OrderItemsJpaRepository;

import java.util.List;

@Service
public class OrderItemsServiceImpl implements OrderItemsService {

    @Autowired
    private OrderItemsJpaRepository orderItemsJpaRepository;


    @Override
    public OrderItems createOrderItems(OrderItems orderItems) {
        return orderItemsJpaRepository.save(orderItems);
    }

    @Override
    public List<OrderItems> getAllOrderItems() {
        return orderItemsJpaRepository.findAll();
    }

    @Override
    public OrderItems getByIdOrderItems(Long id) {
        return orderItemsJpaRepository.findById(id)
                .orElseThrow(() -> new OrderItemsNotFoundException("Order item with id " + id + " not Found"));
    }
}

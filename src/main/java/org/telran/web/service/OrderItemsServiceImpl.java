package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.web.entity.OrderItems;
import org.telran.web.exception.OrderItemsNotFoundException;
import org.telran.web.repository.OrderItemsJpaRepository;
import java.util.Collection;


@Service
public class OrderItemsServiceImpl implements OrderItemsService {

    @Autowired
    private OrderItemsJpaRepository orderItemsJpaRepository;

    @Override
    @Transactional
    public OrderItems createOrderItems(OrderItems entity) {
        return orderItemsJpaRepository.save(entity);
    }

    @Override
    public Collection<OrderItems> getAllOrderItems() {
        return orderItemsJpaRepository.findAll();
    }

    @Override
    public OrderItems getByIdOrderItems(Long id) {
        return orderItemsJpaRepository.findById(id)
                .orElseThrow(() -> new OrderItemsNotFoundException("OrderItem with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void deleteOrderItems(Long id) {
        OrderItems orderItem = orderItemsJpaRepository.findById(id)
                .orElseThrow(() -> new OrderItemsNotFoundException("OrderItem with id " + id + " not found"));
        orderItemsJpaRepository.delete(orderItem);
    }
}

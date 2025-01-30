package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Orders;
import org.telran.web.exception.OrderNotFoundException;
import org.telran.web.exception.UserNotFoundException;
import org.telran.web.repository.OrdersRepository;

import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository repository;

    @Override
    public Orders create(Orders orders) {
        return repository.save(orders);
    }

    @Override
    public List<Orders> getAll() {
        return repository.findAll();
    }

    @Override
    public Orders getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
    }
}

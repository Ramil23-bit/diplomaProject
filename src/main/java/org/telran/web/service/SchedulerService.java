package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telran.web.entity.Orders;
import org.telran.web.enums.OrderStatus;
import org.telran.web.repository.OrdersRepository;

import java.util.List;
@EnableAsync
@Component
public class SchedulerService {

    @Autowired
    private OrdersRepository ordersRepository;
    @Async
    @Scheduled(fixedRate = 10000L)
    public void updateOrderStatusToPaid(){
        List<Orders> ordersServiceList = ordersRepository.findAll();
        for(Orders orders : ordersServiceList){
            if(orders.getStatus().equals(OrderStatus.CREATED)){
                orders.setStatus(OrderStatus.PAID);
                ordersRepository.save(orders);
            }
        }
    }

    @Async
    @Scheduled(fixedRate = 15000L)
    public void updateOrderStatusToCompleted(){
        List<Orders> ordersServiceList = ordersRepository.findAll();
        for(Orders orders : ordersServiceList){
            if(orders.getStatus().equals(OrderStatus.PAID)){
                orders.setStatus(OrderStatus.COMPLETED);
                ordersRepository.save(orders);
            }
        }
    }

    @Async
    @Scheduled(fixedRate = 20000L)
    public void updateOrderStatusToCanceled(){
        List<Orders> ordersServiceList = ordersRepository.findAll();
        for(Orders orders : ordersServiceList){
            if(orders.getStatus().equals(OrderStatus.COMPLETED)){
                orders.setStatus(OrderStatus.CANCELLED);
                ordersRepository.save(orders);
            }
        }
    }
}

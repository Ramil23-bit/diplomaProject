package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrdersSchedulerService {

    @Autowired
    private OrdersServiceImpl ordersService;

    @Scheduled(fixedDelay = 45000L)
    public void updateOrderStatusInOrders() {
       ordersService.updateStatusOrders();
    }

}

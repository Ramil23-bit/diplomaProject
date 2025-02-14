package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentSchedulerService {
    @Autowired
    private PaymentServiceImpl paymentService;
//    @Scheduled(fixedDelay = 30000L)
//    public void updateOrderStatusInPayment(){
//        paymentService.updateStatusPayment();
//    }
}

package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.OrderCreateDto;
import org.telran.web.dto.OrderResponseDto;
import org.telran.web.entity.Orders;
import org.telran.web.service.OrderItemsService;

import java.util.stream.Collectors;

@Component
public class OrderCreateConverter implements Converter<Orders, OrderCreateDto, OrderResponseDto>{

    @Override
    public OrderResponseDto toDto(Orders orders) {
        return new OrderResponseDto(orders.getId(),
                orders.getUser(),
                orders.getOrderItems(),
                orders.getCreatedAt(),
                orders.getDeliveryAddress(),
                orders.getContactPhone(),
                orders.getDeliveryMethod(),
                orders.getStatus(),
                orders.getUpdatedAt()
                );
    }

    @Override
    public Orders toEntity(OrderCreateDto orderCreateDto) {
        return new Orders(
                orderCreateDto.getOrderItems(),
                orderCreateDto.getDeliveryAddress(),
                orderCreateDto.getDeliveryMethod()
        );
    }
}

package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.OrderItemsCreateDto;
import org.telran.web.dto.OrderItemsResponseDto;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Product;
import org.telran.web.service.OrderItemsService;
import org.telran.web.service.ProductService;

@Component
public class OrderItemsConverter implements Converter<OrderItems, OrderItemsCreateDto, OrderItemsResponseDto> {

    @Autowired
    private OrderItemsService orderItemService;

    @Autowired
    private ProductService productService;

    @Override
    public OrderItemsResponseDto toDto(OrderItems orderItems){
        return new OrderItemsResponseDto(orderItems.getId(), orderItems.getQuantity(), orderItems.getPriceAtPurchase(), orderItems.getProduct());
    }

    public OrderItems toEntity(OrderItemsCreateDto orderItemsCreateDto) {
        Product product = productService.getById(orderItemsCreateDto.getProductId());
        return new OrderItems(orderItemsCreateDto.getQuantity(), orderItemsCreateDto.getPriceByPurchase(), product);
    }
}

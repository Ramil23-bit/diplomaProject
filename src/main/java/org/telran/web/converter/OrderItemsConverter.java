package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.OrderItemsCreateDto;
import org.telran.web.dto.OrderItemsResponseDto;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Orders;
import org.telran.web.entity.Product;
import org.telran.web.service.OrderItemsService;
import org.telran.web.service.OrdersService;
import org.telran.web.service.ProductService;

/**
 * Converter class for transforming OrderItems entities to DTOs and vice versa.
 * Handles the conversion between OrderItems, OrderItemsCreateDto, and OrderItemsResponseDto.
 */
@Component
public class OrderItemsConverter implements Converter<OrderItems, OrderItemsCreateDto, OrderItemsResponseDto> {

    @Autowired
    private OrderItemsService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrdersService ordersService;

    /**
     * Converts an OrderItems entity to an OrderItemsResponseDto.
     *
     * @param orderItems The OrderItems entity to convert.
     * @return An OrderItemsResponseDto representing the order item.
     */
    @Override
    public OrderItemsResponseDto toDto(OrderItems orderItems) {
        return new OrderItemsResponseDto(
                orderItems.getId(),
                orderItems.getQuantity(),
                orderItems.getPriceAtPurchase(),
                orderItems.getProduct(),
                orderItems.getOrders());
    }

    /**
     * Converts an OrderItemsCreateDto to an OrderItems entity.
     *
     * @param orderItemsCreateDto The DTO containing order item creation data.
     * @return The created OrderItems entity.
     */
    @Override
    public OrderItems toEntity(OrderItemsCreateDto orderItemsCreateDto) {
        Product product = productService.getById(orderItemsCreateDto.getProductId());
        return new OrderItems(orderItemsCreateDto.getQuantity(), orderItemsCreateDto.getPriceAtPurchase(), productService.getById(orderItemsCreateDto.getProductId()));
    }
}

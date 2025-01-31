package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.OrderItemsCreateDto;
import org.telran.web.dto.OrderItemsResponseDto;
import org.telran.web.entity.OrderItems;
import org.telran.web.service.OrderItemsService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/order_items")
public class OrderItemsController {

    @Autowired
    private OrderItemsService orderItemsService;

    @Autowired
    private Converter<OrderItems, OrderItemsCreateDto, OrderItemsResponseDto> orderItemsConverter;

    @PostMapping
    public OrderItemsResponseDto create(@RequestBody OrderItemsCreateDto orderItemsCreateDto){
        return orderItemsConverter.toDto(
                orderItemsService.createOrderItems(
                        orderItemsConverter.toEntity(orderItemsCreateDto)));
    }

    @GetMapping
    public List<OrderItemsResponseDto> getAll(){
        return orderItemsService.getAllOrderItems().stream()
                .map(orderItem -> orderItemsConverter.toDto(orderItem))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderItemsResponseDto getById(@PathVariable(name = "id") Long id){
        return orderItemsConverter.toDto(orderItemsService.getByIdOrderItems(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        orderItemsService.deleteOrderItems(id);
    }
}

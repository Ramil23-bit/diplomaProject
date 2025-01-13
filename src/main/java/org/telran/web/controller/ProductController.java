package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.ProductConverter;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.entity.Product;
import org.telran.web.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductConverter<Product, ProductCreateDto, ProductResponseDto> createConverter;

   @PostMapping
    public ProductResponseDto create(@RequestBody ProductCreateDto productDto) {
       Product product = createConverter.toEntity(productDto);
       Product productFromDatabase = productService.create(product);
       ProductResponseDto productResponseDto = createConverter.toDto(productFromDatabase);
       return productResponseDto;
   }

   @GetMapping
   public List<Product>getAll(){
       return productService.getAll();
   }

   @GetMapping("/{id}")
   public Product getById(@PathVariable Long id) {
       return productService.getById(id);
   }
}

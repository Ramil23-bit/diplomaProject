package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.entity.Product;
import org.telran.web.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Converter<Product, ProductCreateDto, ProductResponseDto> createConverter;

   @PostMapping
    public ProductResponseDto create(@RequestBody ProductCreateDto productDto) {
       Product product = createConverter.toEntity(productDto);
       Product productFromDatabase = productService.create(product);
       return createConverter.toDto(productFromDatabase);
   }

   @GetMapping
   public List<Product>getAll(){
       return productService.getAll();
   }

   @GetMapping("/{id}")
   public Product getById(@PathVariable Long id) {
       return productService.getById(id);
   }

   @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") Long id, @RequestBody @Valid Product product){
       productService.editProducts(id, product);
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
   }

   @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") Long id){
       productService.deleteProductsById(id);
   }
}

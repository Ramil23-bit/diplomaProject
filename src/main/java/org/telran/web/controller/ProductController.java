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
import org.telran.web.exception.CategoryNotFoundException;
import org.telran.web.exception.StorageNotFoundException;
import org.telran.web.service.CategoryService;
import org.telran.web.service.ProductService;
import org.telran.web.service.StorageService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Converter<Product, ProductCreateDto, ProductResponseDto> createConverter;

   @PostMapping
    public ProductResponseDto create(@Valid @RequestBody ProductCreateDto productDto) {
       return createConverter.toDto(productService.create(createConverter.toEntity(productDto)));
   }

   @GetMapping
   public List<ProductResponseDto>getAll(){
       return productService.getAll().stream()
               .map(product -> createConverter.toDto(product))
               .collect(Collectors.toList());
   }

   @GetMapping("/{discount}")
   public List<Product> getAllProductDiscount(@PathVariable(name = "discount") BigDecimal discount){
       return productService.getAllDiscount(discount);
   }

   @GetMapping("/{id}")
   public ProductResponseDto getById(@PathVariable Long id) {
       return createConverter.toDto(productService.getById(id));
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

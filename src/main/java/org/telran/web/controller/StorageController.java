package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;

import org.telran.web.converter.Converter;
import org.telran.web.dto.StorageCreateDto;
import org.telran.web.dto.StorageResponseDto;
import org.telran.web.entity.Storage;
import org.telran.web.service.StorageService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private Converter<Storage, StorageCreateDto, StorageResponseDto> storageConverter;

    @GetMapping
    public List<StorageResponseDto> getAll(){
        return storageService.getAllStorage().stream()
                .map(storage -> storageConverter.toDto(storage))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StorageResponseDto getById(@PathVariable(name = "id") Long id){
        return storageConverter.toDto(storageService.getByIdStorage(id));
    }

    @PostMapping
    public StorageResponseDto create(@RequestBody StorageCreateDto storageDto){
        return storageConverter.toDto(storageService.createStorage(storageConverter.toEntity(storageDto)));
    }
}

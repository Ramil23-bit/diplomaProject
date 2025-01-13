package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.StorageConverter;
import org.telran.web.dto.StorageCreateDto;
import org.telran.web.dto.StorageResponseDto;
import org.telran.web.entity.Storage;
import org.telran.web.service.StorageService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageConverter<Storage, StorageCreateDto, StorageResponseDto> storageConverter;

    @GetMapping
    public List<Storage> getAll(){
        return storageService.getAllStorage();
    }

    @GetMapping("/{id}")
    public Storage getById(@PathVariable(name = "id") Long id){
        return storageService.getByIdStorage(id);
    }

    @PostMapping
    public StorageResponseDto create(@RequestBody StorageCreateDto storageDto){
        return storageConverter.toDto(storageService.createStorage(storageConverter.toEntity(storageDto)));
    }
}

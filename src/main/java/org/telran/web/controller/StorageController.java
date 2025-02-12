package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.StorageCreateDto;
import org.telran.web.dto.StorageResponseDto;
import org.telran.web.entity.Storage;
import org.telran.web.service.StorageService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing storage.
 * Provides endpoints for creating, retrieving, and listing storage items.
 */
@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private Converter<Storage, StorageCreateDto, StorageResponseDto> storageConverter;

    /**
     * Retrieves all storage items.
     *
     * @return List of StorageResponseDto representing all storage items.
     */
    @GetMapping
    public List<StorageResponseDto> getAll(){
        return storageService.getAllStorage().stream()
                .map(storageConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a storage item by its ID.
     *
     * @param id ID of the storage item.
     * @return StorageResponseDto representing the found storage item.
     */
    @GetMapping("/{id}")
    public StorageResponseDto getById(@PathVariable (name = "id") Long id){
        return storageConverter.toDto(storageService.getByIdStorage(id));
    }

    /**
     * Creates a new storage item.
     *
     * @param storageDto DTO containing the storage item details.
     * @return StorageResponseDto representing the created storage item.
     */
    @PostMapping
    public StorageResponseDto create(@RequestBody StorageCreateDto storageDto){
        return storageConverter.toDto(storageService.createStorage(storageConverter.toEntity(storageDto)));
    }
}

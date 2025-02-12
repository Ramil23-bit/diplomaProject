package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
 * Provides endpoints to create, retrieve, and manage storage items.
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
     * @return List of storage response DTOs.
     */
    @Operation(summary = "Get all storage items", description = "Retrieves a list of all storage items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Storage items successfully retrieved")
    })
    @GetMapping
    public List<StorageResponseDto> getAll() {
        return storageService.getAllStorage().stream()
                .map(storageConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a storage item by its ID.
     *
     * @param id The ID of the storage item.
     * @return The storage response DTO.
     */
    @Operation(summary = "Get storage item by ID", description = "Retrieves details of a specific storage item by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Storage item successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Storage item not found")
    })
    @GetMapping("/{id}")
    public StorageResponseDto getById(@PathVariable(name = "id") Long id) {
        return storageConverter.toDto(storageService.getByIdStorage(id));
    }

    /**
     * Creates a new storage entry.
     *
     * @param storageDto The DTO containing storage details.
     * @return The created storage response DTO.
     */
    @Operation(summary = "Create a new storage entry", description = "Adds a new item to storage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Storage item successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StorageResponseDto create(@RequestBody StorageCreateDto storageDto) {
        return storageConverter.toDto(storageService.createStorage(storageConverter.toEntity(storageDto)));
    }
}
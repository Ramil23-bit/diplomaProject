package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Provides endpoints to create, retrieve, and list storage records.
 */
@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    private static final Logger logger = LoggerFactory.getLogger(StorageController.class);

    @Autowired
    private StorageService storageService;

    @Autowired
    private Converter<Storage, StorageCreateDto, StorageResponseDto> storageConverter;

    /**
     * Retrieves all storage records.
     *
     * @return List of storage response DTOs.
     */
    @Operation(summary = "Get all storage records", description = "Retrieves a list of all storage records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Storage records successfully retrieved")
    })
    @GetMapping
    public List<StorageResponseDto> getAll() {
        logger.info("Fetching all storage records");
        List<StorageResponseDto> storageList = storageService.getAllStorage().stream()
                .map(storageConverter::toDto)
                .collect(Collectors.toList());
        logger.info("Total storage records retrieved: {}", storageList.size());
        return storageList;
    }

    /**
     * Retrieves a storage record by its ID.
     *
     * @param id The ID of the storage record.
     * @return The storage response DTO.
     */
    @Operation(summary = "Get storage record by ID", description = "Retrieves details of a specific storage record by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Storage record successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Storage record not found")
    })
    @GetMapping("/{id}")
    public StorageResponseDto getById(@PathVariable(name = "id") Long id) {
        logger.info("Fetching storage record with ID: {}", id);
        StorageResponseDto storage = storageConverter.toDto(storageService.getByIdStorage(id));
        logger.info("Storage record retrieved: {}", storage);
        return storage;
    }

    /**
     * Creates a new storage record.
     *
     * @param storageDto The DTO containing storage details.
     * @return The created storage response DTO.
     */
    @Operation(summary = "Create a new storage record", description = "Registers a new storage entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Storage record successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StorageResponseDto create(@RequestBody StorageCreateDto storageDto) {
        logger.info("Received request to create storage record: {}", storageDto);
        StorageResponseDto response = storageConverter.toDto(storageService.createStorage(storageConverter.toEntity(storageDto)));
        logger.info("Storage record created successfully with ID: {}", response.getId());
        return response;
    }
}

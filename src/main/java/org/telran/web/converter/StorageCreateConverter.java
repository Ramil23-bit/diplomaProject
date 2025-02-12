package org.telran.web.converter;

import org.springframework.stereotype.Component;
import org.telran.web.dto.StorageCreateDto;
import org.telran.web.dto.StorageResponseDto;
import org.telran.web.entity.Storage;

/**
 * Converter class for transforming Storage entities to DTOs and vice versa.
 * Handles the conversion between Storage, StorageCreateDto, and StorageResponseDto.
 */
@Component
public class StorageCreateConverter implements Converter<Storage, StorageCreateDto, StorageResponseDto> {

    /**
     * Converts a Storage entity to a StorageResponseDto.
     *
     * @param storage The Storage entity to convert.
     * @return A StorageResponseDto representing the storage details.
     */
    @Override
    public StorageResponseDto toDto(Storage storage) {
        return new StorageResponseDto(storage.getId(), storage.getAmount());
    }

    /**
     * Converts a StorageCreateDto to a Storage entity.
     *
     * @param storageCreateDto The DTO containing storage creation data.
     * @return The created Storage entity.
     */
    @Override
    public Storage toEntity(StorageCreateDto storageCreateDto) {
        return new Storage(storageCreateDto.getAmount());
    }
}
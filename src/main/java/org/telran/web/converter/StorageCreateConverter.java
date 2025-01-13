package org.telran.web.converter;

import org.springframework.stereotype.Component;
import org.telran.web.dto.StorageCreateDto;
import org.telran.web.dto.StorageResponseDto;
import org.telran.web.entity.Storage;
@Component
public class StorageCreateConverter implements Converter<Storage, StorageCreateDto, StorageResponseDto>{
    @Override
    public StorageResponseDto toDto(Storage storage) {
        return new StorageResponseDto(storage.getId(), storage.getAmount());
    }

    @Override
    public Storage toEntity(StorageCreateDto storageCreateDto) {
        return new Storage(storageCreateDto.getAmount(), storageCreateDto.getProductList());
    }
}

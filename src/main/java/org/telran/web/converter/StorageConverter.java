package org.telran.web.converter;

public interface StorageConverter<Entity, RequestDto, ResponseDto> {

    ResponseDto toDto(Entity entity);


    Entity toEntity(RequestDto dto);
}

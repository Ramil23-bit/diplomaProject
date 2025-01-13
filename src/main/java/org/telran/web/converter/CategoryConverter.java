package org.telran.web.converter;

public interface CategoryConverter<Entity, RequestDto, ResponseDto> {

    ResponseDto toDto(Entity entity);


    Entity toEntity(RequestDto dto);
}

package org.telran.web.converter;

public interface ProductConverter<Entity, RequestDto, ResponseDto>{

    ResponseDto toDto(Entity entity);


    Entity toEntity(RequestDto dto);
}

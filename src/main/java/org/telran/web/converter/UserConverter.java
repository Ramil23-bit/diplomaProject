package org.telran.web.converter;

public interface UserConverter <Entity, RequestDto, ResponseDto>{

    ResponseDto toDto(Entity entity);
    Entity toEntity(RequestDto requestDto);
}

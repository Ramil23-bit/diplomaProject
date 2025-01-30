package org.telran.web.converter;

public interface Converter <Entity, RequestDto, ResponseDto>{

    ResponseDto toDto(Entity entity);
    Entity toEntity(RequestDto requestDto);

}

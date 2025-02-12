package org.telran.web.converter;

/**
 * Generic Converter interface for transforming entities to DTOs and vice versa.
 *
 * @param <Entity> The entity type.
 * @param <RequestDto> The DTO type for entity creation.
 * @param <ResponseDto> The DTO type for entity response.
 */
public interface Converter<Entity, RequestDto, ResponseDto> {

    /**
     * Converts an entity to a response DTO.
     *
     * @param entity The entity to convert.
     * @return The corresponding response DTO.
     */
    ResponseDto toDto(Entity entity);

    /**
     * Converts a request DTO to an entity.
     *
     * @param requestDto The request DTO to convert.
     * @return The corresponding entity.
     */
    Entity toEntity(RequestDto requestDto);
}

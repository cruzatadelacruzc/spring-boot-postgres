package com.example.oauth2.service.mapper;

import java.util.List;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <E> - domain type parameter.
 */
public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto( E entity);

    List <E> toEntity(List<D> dtoList);

    List <D> toDto(List<E> entityList);
}

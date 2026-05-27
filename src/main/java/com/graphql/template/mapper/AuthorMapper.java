package com.graphql.template.mapper;

import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.entity.AuthorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(source = "name", target = "firstName")
    @Mapping(source = "id", target = "id")
    AuthorDTO toDto(AuthorEntity entity);

    @Mapping(source = "firstName", target = "name")
    @Mapping(source = "id", target = "id")
    @Mapping(target = "books", ignore = true)
    AuthorEntity toEntity(AuthorDTO dto);

    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    default UUID stringToUuid(String id) {
        return id != null ? UUID.fromString(id) : null;
    }
}
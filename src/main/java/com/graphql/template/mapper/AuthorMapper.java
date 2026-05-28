package com.graphql.template.mapper;

import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.dto.AuthorInput;
import com.graphql.template.entity.AuthorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(source = "name", target = "firstName")
    AuthorDTO toDto(AuthorEntity entity);

    @Mapping(source = "firstName", target = "name")
    @Mapping(target = "books", ignore = true)
    AuthorEntity toEntity(AuthorDTO dto);

    @Mapping(source = "firstName", target = "name")
    @Mapping(target = "books", ignore = true)
    AuthorEntity toEntity(AuthorInput input);
}
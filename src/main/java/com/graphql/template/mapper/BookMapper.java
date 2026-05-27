package com.graphql.template.mapper;

import com.graphql.template.dto.BookDTO;
import com.graphql.template.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "bookId", target = "id")
    @Mapping(source = "author.id", target = "authorId")
    BookDTO toDto(BookEntity entity);

    @Mapping(source = "id", target = "bookId")
    @Mapping(target = "author", ignore = true)
    BookEntity toEntity(BookDTO dto);
}
package com.graphql.template.mapper;

import com.graphql.template.dto.BookDTO;
import com.graphql.template.dto.BookInput;
import com.graphql.template.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "bookId", target = "id")
    @Mapping(target = "authorId", expression = "java(entity.getAuthor() != null ? entity.getAuthor().getId() : null)")
    BookDTO toDto(BookEntity entity);

    @Mapping(source = "id", target = "bookId")
    @Mapping(target = "author", ignore = true)
    BookEntity toEntity(BookDTO dto);

    @Mapping(target = "bookId", ignore = true)
    @Mapping(target = "author", ignore = true)
    BookEntity toEntity(BookInput input);
}
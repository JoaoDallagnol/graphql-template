package com.graphql.template.repository;

import com.graphql.template.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Query("""
        SELECT b
        FROM BookEntity b
        WHERE (:id IS NULL OR b.bookId = :id)
          AND (:authorId IS NULL OR b.author.id = :authorId)
          AND (:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    List<BookEntity> findBooksWithFilters(
            @Param("id") Long id,
            @Param("authorId") String authorId,
            @Param("name") String name
    );

    @Query("""
        SELECT b
        FROM BookEntity b
        WHERE b.author.id = :authorId
    """)
    List<BookEntity> findBooksByAuthorId(
            @Param("authorId") Long authorId
    );
}
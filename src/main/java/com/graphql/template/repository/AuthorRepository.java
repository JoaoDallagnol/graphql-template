package com.graphql.template.repository;

import com.graphql.template.entity.AuthorEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    /**
     * Forward pagination: retrieves items after a cursor in ascending order.
     * Cursor represents an ID boundary. Used with first parameter.
     *
     * @param cursorId the ID to start after
     * @param pageable page size (typically first + 1 for lookahead)
     * @return items ordered by ID ascending, starting after cursorId
     */
    List<AuthorEntity> findByIdGreaterThanOrderByIdAsc(Long cursorId, Pageable pageable);

    /**
     * Backward pagination: retrieves items before a cursor in descending order.
     * Returns DESC ordered results; client reverses them to ASC.
     * Used with last parameter.
     *
     * @param cursorId the ID to start before
     * @param pageable page size (typically last + 1 for lookahead)
     * @return items ordered by ID descending, starting before cursorId
     */
    List<AuthorEntity> findByIdLessThanOrderByIdDesc(Long cursorId, Pageable pageable);

    /**
     * First page forward: retrieves initial items in ascending order.
     * No cursor provided — starts from beginning.
     *
     * @param pageable page size (typically first + 1 for lookahead)
     * @return items ordered by ID ascending, from start
     */
    List<AuthorEntity> findByOrderByIdAsc(Pageable pageable);

    /**
     * Last page backward: retrieves final items in descending order.
     * No cursor provided — starts from end. Results need reversing.
     *
     * @param pageable page size (typically last + 1 for lookahead)
     * @return items ordered by ID descending, from end
     */
    List<AuthorEntity> findByOrderByIdDesc(Pageable pageable);
}

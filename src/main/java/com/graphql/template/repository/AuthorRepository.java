package com.graphql.template.repository;

import com.graphql.template.entity.AuthorEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    // Forward pagination: items after the cursor
    List<AuthorEntity> findByIdGreaterThanOrderByIdAsc(Long cursorId, Pageable pageable);

    // Backward pagination: items before the cursor
    List<AuthorEntity> findByIdLessThanOrderByIdDesc(Long cursorId, Pageable pageable);

    // First page (no cursor) — forward
    List<AuthorEntity> findByOrderByIdAsc(Pageable pageable);

    // Last page (no cursor) — backward
    List<AuthorEntity> findByOrderByIdDesc(Pageable pageable);
}

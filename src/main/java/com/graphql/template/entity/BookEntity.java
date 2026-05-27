package com.graphql.template.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int pageCount;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AuthorEntity author;
}

package com.graphql.template.data;

import java.util.Arrays;
import java.util.List;

public record Book(String id, String name, int pageCount, String authorId) {
    private static final List<Book> books = Arrays.asList(
            new Book("book-1", "Effective Java", 416, "author-1"),
            new Book("book-2", "Hitchhiker's Guide to the Galaxy", 208, "author-2"),
            new Book("book-3", "Down Under", 436, "author-3"),
            new Book("book-4", "Atomic Habits", 600, "author-2")
    );

    public static Book getById(String id) {
        return books.stream()
                .filter(book -> book.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<Book> getAllBooks() {
        return books;
    }

    public static List<Book> getBooksByAuthor(String id) {
        return books.stream()
                .filter(book -> book.authorId().equals(id))
                .toList();
    }
}

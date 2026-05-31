package com.graphql.template.controller;

import com.graphql.template.config.TestGraphQlConfig;
import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.dto.BookDTO;
import com.graphql.template.dto.BookInput;
import com.graphql.template.constants.ErrorCode;
import com.graphql.template.exception.NotFoundException;
import com.graphql.template.service.AuthorService;
import com.graphql.template.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para BookController usando GraphQlTester.
 * SpringBootTest carrega o contexto completo da aplicação para testes de integração.
 */
@SpringBootTest
@Import(TestGraphQlConfig.class)
class BookControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    // ==================== QUERY TESTS ====================

    @Test
    @DisplayName("Query books: deve retornar todos os livros")
    void testGetAllBooks() {
        // Arrange: prepara dados mockados
        BookDTO book1 = new BookDTO(1L, "Clean Code", 464, 1L);
        BookDTO book2 = new BookDTO(2L, "Effective Java", 416, 2L);
        List<BookDTO> books = Arrays.asList(book1, book2);

        AuthorDTO author1 = new AuthorDTO(1L, "Robert", "Martin");
        AuthorDTO author2 = new AuthorDTO(2L, "Joshua", "Bloch");

        when(bookService.books()).thenReturn(books);
        when(authorService.getById(1L)).thenReturn(author1);
        when(authorService.getById(2L)).thenReturn(author2);

        // Act & Assert: executa query GraphQL e valida resposta
        graphQlTester
                .documentName("book") // arquivo book.graphql em test/resources/graphql
                .operationName("GetAllBooks")
                .execute()
                .path("books")
                .entityList(BookDTO.class)
                .hasSize(2)
                .path("books[0].id").entity(Long.class).isEqualTo(1L)
                .path("books[0].name").entity(String.class).isEqualTo("Clean Code")
                .path("books[0].author.firstName").entity(String.class).isEqualTo("Robert")
                .path("books[1].id").entity(Long.class).isEqualTo(2L);

        verify(bookService, times(1)).books();
        verify(authorService, times(1)).getById(1L);
        verify(authorService, times(1)).getById(2L);
    }

    @Test
    @DisplayName("Query books: deve retornar lista vazia quando não há livros")
    void testGetAllBooksEmpty() {
        // Arrange
        when(bookService.books()).thenReturn(Collections.emptyList());

        // Act & Assert
        graphQlTester
                .documentName("book")
                .operationName("GetAllBooks")
                .execute()
                .path("books")
                .entityList(BookDTO.class)
                .hasSize(0);

        verify(bookService, times(1)).books();
    }

    @Test
    @DisplayName("Query bookById: deve retornar livro quando ID existe")
    void testGetBookById() {
        // Arrange
        Long bookId = 1L;
        BookDTO book = new BookDTO(bookId, "Clean Code", 464, 1L);
        AuthorDTO author = new AuthorDTO(1L, "Robert", "Martin");

        when(bookService.bookById(bookId)).thenReturn(book);
        when(authorService.getById(1L)).thenReturn(author);

        // Act & Assert: testa query com variável
        graphQlTester
                .documentName("book")
                .operationName("GetBookById")
                .variable("id", bookId)
                .execute()
                .path("bookById.id").entity(Long.class).isEqualTo(bookId)
                .path("bookById.name").entity(String.class).isEqualTo("Clean Code")
                .path("bookById.pageCount").entity(Integer.class).isEqualTo(464)
                .path("bookById.author.firstName").entity(String.class).isEqualTo("Robert");

        verify(bookService, times(1)).bookById(bookId);
        verify(authorService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Query bookById: deve retornar erro quando ID não existe")
    void testGetBookByIdNotFound() {
        // Arrange
        Long bookId = 999L;
        when(bookService.bookById(bookId)).thenThrow(new NotFoundException(ErrorCode.BOOK_NOT_FOUND));

        // Act & Assert: valida que erro GraphQL é retornado
        graphQlTester
                .documentName("book")
                .operationName("GetBookById")
                .variable("id", bookId)
                .execute()
                .errors()
                .expect(error -> error.getMessage().contains("Book not found"));

        verify(bookService, times(1)).bookById(bookId);
    }

    @Test
    @DisplayName("Query booksByAuthor: deve retornar livros do autor")
    void testGetBooksByAuthor() {
        // Arrange
        Long authorId = 1L;
        BookDTO book1 = new BookDTO(1L, "Clean Code", 464, authorId);
        BookDTO book2 = new BookDTO(2L, "Clean Architecture", 432, authorId);
        List<BookDTO> books = Arrays.asList(book1, book2);
        AuthorDTO author = new AuthorDTO(authorId, "Robert", "Martin");

        when(bookService.booksByAuthor(authorId)).thenReturn(books);
        when(authorService.getById(authorId)).thenReturn(author);

        // Act & Assert
        graphQlTester
                .documentName("book")
                .operationName("GetBooksByAuthor")
                .variable("id", authorId)
                .execute()
                .path("booksByAuthor")
                .entityList(BookDTO.class)
                .hasSize(2)
                .path("booksByAuthor[0].author.id").entity(Long.class).isEqualTo(authorId)
                .path("booksByAuthor[1].author.id").entity(Long.class).isEqualTo(authorId);

        verify(bookService, times(1)).booksByAuthor(authorId);
        verify(authorService, times(2)).getById(authorId);
    }

    @Test
    @DisplayName("Query booksByAuthor: deve retornar lista vazia quando autor não tem livros")
    void testGetBooksByAuthorEmpty() {
        // Arrange
        Long authorId = 1L;
        when(bookService.booksByAuthor(authorId)).thenReturn(Collections.emptyList());

        // Act & Assert
        graphQlTester
                .documentName("book")
                .operationName("GetBooksByAuthor")
                .variable("id", authorId)
                .execute()
                .path("booksByAuthor")
                .entityList(BookDTO.class)
                .hasSize(0);

        verify(bookService, times(1)).booksByAuthor(authorId);
    }

    @Test
    @DisplayName("Query booksWithFilter: deve filtrar por ID")
    void testGetBooksWithFilterById() {
        // Arrange
        Long bookId = 1L;
        BookDTO book = new BookDTO(bookId, "Clean Code", 464, 1L);
        AuthorDTO author = new AuthorDTO(1L, "Robert", "Martin");

        when(bookService.books(eq(bookId), isNull(), isNull())).thenReturn(List.of(book));
        when(authorService.getById(1L)).thenReturn(author);

        // Act & Assert: testa filtro com apenas ID
        graphQlTester
                .documentName("book")
                .operationName("GetBooksWithFilter")
                .variable("id", bookId)
                .execute()
                .path("booksWithFilter")
                .entityList(BookDTO.class)
                .hasSize(1)
                .path("booksWithFilter[0].id").entity(Long.class).isEqualTo(bookId);

        verify(bookService, times(1)).books(eq(bookId), isNull(), isNull());
    }

    @Test
    @DisplayName("Query booksWithFilter: deve filtrar por nome")
    void testGetBooksWithFilterByName() {
        // Arrange
        String bookName = "Clean";
        BookDTO book = new BookDTO(1L, "Clean Code", 464, 1L);
        AuthorDTO author = new AuthorDTO(1L, "Robert", "Martin");

        when(bookService.books(isNull(), isNull(), eq(bookName))).thenReturn(List.of(book));
        when(authorService.getById(1L)).thenReturn(author);

        // Act & Assert
        graphQlTester
                .documentName("book")
                .operationName("GetBooksWithFilter")
                .variable("name", bookName)
                .execute()
                .path("booksWithFilter")
                .entityList(BookDTO.class)
                .hasSize(1)
                .path("booksWithFilter[0].name").entity(String.class).isEqualTo("Clean Code");

        verify(bookService, times(1)).books(isNull(), isNull(), eq(bookName));
    }

    @Test
    @DisplayName("Query booksWithFilter: deve filtrar por authorId")
    void testGetBooksWithFilterByAuthorId() {
        // Arrange
        String authorId = "1";
        BookDTO book = new BookDTO(1L, "Clean Code", 464, 1L);
        AuthorDTO author = new AuthorDTO(1L, "Robert", "Martin");

        when(bookService.books(isNull(), eq(authorId), isNull())).thenReturn(List.of(book));
        when(authorService.getById(1L)).thenReturn(author);

        // Act & Assert
        graphQlTester
                .documentName("book")
                .operationName("GetBooksWithFilter")
                .variable("authorId", authorId)
                .execute()
                .path("booksWithFilter")
                .entityList(BookDTO.class)
                .hasSize(1);

        verify(bookService, times(1)).books(isNull(), eq(authorId), isNull());
    }

    // ==================== MUTATION TESTS ====================

    @Test
    @DisplayName("Mutation createBook: deve criar livro com sucesso")
    void testCreateBook() {
        // Arrange
        BookInput input = new BookInput("Clean Code", 464, 1L);
        BookDTO createdBook = new BookDTO(1L, "Clean Code", 464, 1L);
        AuthorDTO author = new AuthorDTO(1L, "Robert", "Martin");

        when(bookService.createBook(any(BookInput.class))).thenReturn(createdBook);
        when(authorService.getById(1L)).thenReturn(author);

        // Act & Assert: testa mutation com input object
        graphQlTester
                .documentName("book")
                .operationName("CreateBook")
                .variable("book", input)
                .execute()
                .path("createBook.id").entity(Long.class).isEqualTo(1L)
                .path("createBook.name").entity(String.class).isEqualTo("Clean Code")
                .path("createBook.pageCount").entity(Integer.class).isEqualTo(464)
                .path("createBook.author.firstName").entity(String.class).isEqualTo("Robert");

        verify(bookService, times(1)).createBook(any(BookInput.class));
        verify(authorService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Mutation createBook: deve retornar erro quando autor não existe")
    void testCreateBookWithInvalidAuthor() {
        // Arrange
        BookInput input = new BookInput("Clean Code", 464, 999L);
        when(bookService.createBook(any(BookInput.class)))
                .thenThrow(new NotFoundException(ErrorCode.AUTHOR_NOT_FOUND));

        // Act & Assert
        graphQlTester
                .documentName("book")
                .operationName("CreateBook")
                .variable("book", input)
                .execute()
                .errors()
                .expect(error -> error.getMessage().contains("Author not found"));

        verify(bookService, times(1)).createBook(any(BookInput.class));
    }

    @Test
    @DisplayName("Mutation updateBook: deve atualizar livro com sucesso")
    void testUpdateBook() {
        // Arrange
        Long bookId = 1L;
        BookInput input = new BookInput("Clean Code Updated", 500, 1L);
        BookDTO updatedBook = new BookDTO(bookId, "Clean Code Updated", 500, 1L);
        AuthorDTO author = new AuthorDTO(1L, "Robert", "Martin");

        when(bookService.updateBook(eq(bookId), any(BookInput.class))).thenReturn(updatedBook);
        when(authorService.getById(1L)).thenReturn(author);

        // Act & Assert
        graphQlTester
                .documentName("book")
                .operationName("UpdateBook")
                .variable("id", bookId)
                .variable("book", input)
                .execute()
                .path("updateBook.id").entity(Long.class).isEqualTo(bookId)
                .path("updateBook.name").entity(String.class).isEqualTo("Clean Code Updated")
                .path("updateBook.pageCount").entity(Integer.class).isEqualTo(500);

        verify(bookService, times(1)).updateBook(eq(bookId), any(BookInput.class));
        verify(authorService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Mutation updateBook: deve retornar erro quando livro não existe")
    void testUpdateBookNotFound() {
        // Arrange
        Long bookId = 999L;
        BookInput input = new BookInput("Clean Code", 464, 1L);
        when(bookService.updateBook(eq(bookId), any(BookInput.class)))
                .thenThrow(new NotFoundException(ErrorCode.BOOK_NOT_FOUND));

        // Act & Assert
        graphQlTester
                .documentName("book")
                .operationName("UpdateBook")
                .variable("id", bookId)
                .variable("book", input)
                .execute()
                .errors()
                .expect(error -> error.getMessage().contains("Book not found"));

        verify(bookService, times(1)).updateBook(eq(bookId), any(BookInput.class));
    }

    @Test
    @DisplayName("Mutation deleteBook: deve deletar livro com sucesso")
    void testDeleteBook() {
        // Arrange
        Long bookId = 1L;
        when(bookService.deleteBook(bookId)).thenReturn(bookId);

        // Act & Assert: valida que mutation retorna o ID deletado
        graphQlTester
                .documentName("book")
                .operationName("DeleteBook")
                .variable("id", bookId)
                .execute()
                .path("deleteBook").entity(Long.class).isEqualTo(bookId);

        verify(bookService, times(1)).deleteBook(bookId);
    }

    @Test
    @DisplayName("Mutation deleteBook: deve retornar erro quando livro não existe")
    void testDeleteBookNotFound() {
        // Arrange
        Long bookId = 999L;
        when(bookService.deleteBook(bookId))
                .thenThrow(new NotFoundException(ErrorCode.BOOK_NOT_FOUND));

        // Act & Assert
        graphQlTester
                .documentName("book")
                .operationName("DeleteBook")
                .variable("id", bookId)
                .execute()
                .errors()
                .expect(error -> error.getMessage().contains("Book not found"));

        verify(bookService, times(1)).deleteBook(bookId);
    }

    // ==================== SCHEMA MAPPING TESTS ====================

    @Test
    @DisplayName("SchemaMapping author: deve resolver campo author do Book")
    void testAuthorFieldResolver() {
        // Arrange
        Long bookId = 1L;
        BookDTO book = new BookDTO(bookId, "Clean Code", 464, 1L);
        AuthorDTO author = new AuthorDTO(1L, "Robert", "Martin");

        when(bookService.bookById(bookId)).thenReturn(book);
        when(authorService.getById(1L)).thenReturn(author);

        // Act & Assert: valida que o field resolver carrega o autor corretamente
        graphQlTester
                .documentName("book")
                .operationName("GetBookById")
                .variable("id", bookId)
                .execute()
                .path("bookById.author").hasValue()
                .path("bookById.author.id").entity(Long.class).isEqualTo(1L)
                .path("bookById.author.firstName").entity(String.class).isEqualTo("Robert")
                .path("bookById.author.lastName").entity(String.class).isEqualTo("Martin");

        verify(bookService, times(1)).bookById(bookId);
        verify(authorService, times(1)).getById(1L);
    }
}

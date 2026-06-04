package com.graphql.template.resolver;

import com.graphql.template.config.TestGraphQlConfig;
import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.dto.AuthorInput;
import com.graphql.template.constants.ErrorCode;
import com.graphql.template.exception.DeletionNotAllowedException;
import com.graphql.template.exception.NotFoundException;
import com.graphql.template.service.AuthorService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AuthorController usando GraphQlTester.
 * SpringBootTest carrega o contexto completo da aplicação para testes de integração.
 */
@SpringBootTest
@Import(TestGraphQlConfig.class)
class AuthorControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private AuthorService authorService;

    // ==================== QUERY TESTS ====================

    @Test
    @DisplayName("Query authors: deve retornar todos os autores")
    void testGetAllAuthors() {
        // Arrange: prepara lista de autores mockados
        AuthorDTO author1 = new AuthorDTO(1L, "Robert", "Martin");
        AuthorDTO author2 = new AuthorDTO(2L, "Joshua", "Bloch");
        AuthorDTO author3 = new AuthorDTO(3L, "Martin", "Fowler");
        List<AuthorDTO> authors = Arrays.asList(author1, author2, author3);

        when(authorService.getAllAuthors()).thenReturn(authors);

        // Act & Assert: executa query e valida estrutura da resposta
        graphQlTester
                .documentName("author") // arquivo author.graphql em test/resources/graphql
                .operationName("GetAllAuthors")
                .execute()
                .path("authors")
                .entityList(AuthorDTO.class)
                .hasSize(3)
                .path("authors[0].id").entity(Long.class).isEqualTo(1L)
                .path("authors[0].firstName").entity(String.class).isEqualTo("Robert")
                .path("authors[0].lastName").entity(String.class).isEqualTo("Martin")
                .path("authors[1].id").entity(Long.class).isEqualTo(2L)
                .path("authors[1].firstName").entity(String.class).isEqualTo("Joshua")
                .path("authors[2].id").entity(Long.class).isEqualTo(3L);

        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    @DisplayName("Query authors: deve retornar lista vazia quando não há autores")
    void testGetAllAuthorsEmpty() {
        // Arrange
        when(authorService.getAllAuthors()).thenReturn(Collections.emptyList());

        // Act & Assert: valida que lista vazia é retornada corretamente
        graphQlTester
                .documentName("author")
                .operationName("GetAllAuthors")
                .execute()
                .path("authors")
                .entityList(AuthorDTO.class)
                .hasSize(0);

        verify(authorService, times(1)).getAllAuthors();
    }

    // ==================== MUTATION TESTS ====================

    @Test
    @DisplayName("Mutation createAuthor: deve criar autor com sucesso")
    void testCreateAuthor() {
        // Arrange
        AuthorInput input = new AuthorInput("Robert", "Martin");
        AuthorDTO createdAuthor = new AuthorDTO(1L, "Robert", "Martin");

        when(authorService.createAuthor(any(AuthorInput.class))).thenReturn(createdAuthor);

        // Act & Assert: testa mutation com input object
        graphQlTester
                .documentName("author")
                .operationName("CreateAuthor")
                .variable("author", input)
                .execute()
                .path("createAuthor.id").entity(Long.class).isEqualTo(1L)
                .path("createAuthor.firstName").entity(String.class).isEqualTo("Robert")
                .path("createAuthor.lastName").entity(String.class).isEqualTo("Martin");

        verify(authorService, times(1)).createAuthor(any(AuthorInput.class));
    }

    @Test
    @DisplayName("Mutation createAuthor: deve validar campos obrigatórios")
    void testCreateAuthorWithMissingFields() {
        // Arrange: input sem firstName (campo obrigatório no schema)
        String invalidQuery = """
                mutation {
                    createAuthor(author: { lastName: "Martin" }) {
                        id
                        firstName
                        lastName
                    }
                }
                """;

        // Act & Assert: valida que erro de validação GraphQL é retornado
        graphQlTester
                .document(invalidQuery)
                .execute()
                .errors()
                .satisfy(errors -> {
                    // GraphQL retorna erro de validação de schema
                    assert !errors.isEmpty();
                });
    }

    @Test
    @DisplayName("Mutation updateAuthor: deve atualizar autor com sucesso")
    void testUpdateAuthor() {
        // Arrange
        Long authorId = 1L;
        AuthorInput input = new AuthorInput("Robert", "C. Martin");
        AuthorDTO updatedAuthor = new AuthorDTO(authorId, "Robert", "C. Martin");

        when(authorService.updateAuthor(eq(authorId), any(AuthorInput.class)))
                .thenReturn(updatedAuthor);

        // Act & Assert: valida que dados são atualizados corretamente
        graphQlTester
                .documentName("author")
                .operationName("UpdateAuthor")
                .variable("id", authorId)
                .variable("author", input)
                .execute()
                .path("updateAuthor.id").entity(Long.class).isEqualTo(authorId)
                .path("updateAuthor.firstName").entity(String.class).isEqualTo("Robert")
                .path("updateAuthor.lastName").entity(String.class).isEqualTo("C. Martin");

        verify(authorService, times(1)).updateAuthor(eq(authorId), any(AuthorInput.class));
    }

    @Test
    @DisplayName("Mutation updateAuthor: deve retornar erro quando autor não existe")
    void testUpdateAuthorNotFound() {
        // Arrange
        Long authorId = 999L;
        AuthorInput input = new AuthorInput("Robert", "Martin");
        when(authorService.updateAuthor(eq(authorId), any(AuthorInput.class)))
                .thenThrow(new NotFoundException(ErrorCode.AUTHOR_NOT_FOUND));

        // Act & Assert: valida tratamento de erro
        graphQlTester
                .documentName("author")
                .operationName("UpdateAuthor")
                .variable("id", authorId)
                .variable("author", input)
                .execute()
                .errors()
                .expect(error -> error.getMessage().contains("Author not found"));

        verify(authorService, times(1)).updateAuthor(eq(authorId), any(AuthorInput.class));
    }

    @Test
    @DisplayName("Mutation deleteAuthor: deve deletar autor com sucesso")
    void testDeleteAuthor() {
        // Arrange
        Long authorId = 1L;
        when(authorService.deleteAuthor(authorId)).thenReturn(authorId);

        // Act & Assert: valida que mutation retorna o ID deletado
        graphQlTester
                .documentName("author")
                .operationName("DeleteAuthor")
                .variable("id", authorId)
                .execute()
                .path("deleteAuthor").entity(Long.class).isEqualTo(authorId);

        verify(authorService, times(1)).deleteAuthor(authorId);
    }

    @Test
    @DisplayName("Mutation deleteAuthor: deve retornar erro quando autor não existe")
    void testDeleteAuthorNotFound() {
        // Arrange
        Long authorId = 999L;
        when(authorService.deleteAuthor(authorId))
                .thenThrow(new NotFoundException(ErrorCode.AUTHOR_NOT_FOUND));

        // Act & Assert
        graphQlTester
                .documentName("author")
                .operationName("DeleteAuthor")
                .variable("id", authorId)
                .execute()
                .errors()
                .expect(error -> error.getMessage().contains("Author not found"));

        verify(authorService, times(1)).deleteAuthor(authorId);
    }

    @Test
    @DisplayName("Mutation deleteAuthor: deve retornar erro quando autor tem livros associados")
    void testDeleteAuthorWithBooks() {
        // Arrange: simula autor com livros que não pode ser deletado
        Long authorId = 1L;
        when(authorService.deleteAuthor(authorId))
                .thenThrow(new DeletionNotAllowedException(ErrorCode.DELETION_NOT_ALLOWED_FOR_AUTHOR));

        // Act & Assert: valida regra de negócio
        graphQlTester
                .documentName("author")
                .operationName("DeleteAuthor")
                .variable("id", authorId)
                .execute()
                .errors()
                .expect(error -> error.getMessage().contains("cannot be deleted"));

        verify(authorService, times(1)).deleteAuthor(authorId);
    }

    // ==================== INTEGRATION SCENARIO TESTS ====================

    @Test
    @DisplayName("Cenário: criar e atualizar autor em sequência")
    void testCreateAndUpdateAuthorScenario() {
        // Arrange: simula fluxo completo de criação e atualização
        AuthorInput createInput = new AuthorInput("Robert", "Martin");
        AuthorDTO createdAuthor = new AuthorDTO(1L, "Robert", "Martin");
        
        AuthorInput updateInput = new AuthorInput("Robert", "C. Martin");
        AuthorDTO updatedAuthor = new AuthorDTO(1L, "Robert", "C. Martin");

        when(authorService.createAuthor(any(AuthorInput.class))).thenReturn(createdAuthor);
        when(authorService.updateAuthor(eq(1L), any(AuthorInput.class))).thenReturn(updatedAuthor);

        // Act & Assert: cria autor
        graphQlTester
                .documentName("author")
                .operationName("CreateAuthor")
                .variable("author", createInput)
                .execute()
                .path("createAuthor.id").entity(Long.class).isEqualTo(1L)
                .path("createAuthor.lastName").entity(String.class).isEqualTo("Martin");

        // Act & Assert: atualiza autor criado
        graphQlTester
                .documentName("author")
                .operationName("UpdateAuthor")
                .variable("id", 1L)
                .variable("author", updateInput)
                .execute()
                .path("updateAuthor.lastName").entity(String.class).isEqualTo("C. Martin");

        verify(authorService, times(1)).createAuthor(any(AuthorInput.class));
        verify(authorService, times(1)).updateAuthor(eq(1L), any(AuthorInput.class));
    }

    @Test
    @DisplayName("Cenário: listar autores após criação")
    void testListAuthorsAfterCreation() {
        // Arrange: simula estado após criação de autores
        AuthorDTO author1 = new AuthorDTO(1L, "Robert", "Martin");
        AuthorDTO author2 = new AuthorDTO(2L, "Joshua", "Bloch");
        List<AuthorDTO> authors = Arrays.asList(author1, author2);

        when(authorService.getAllAuthors()).thenReturn(authors);

        // Act & Assert: valida que lista contém autores criados
        graphQlTester
                .documentName("author")
                .operationName("GetAllAuthors")
                .execute()
                .path("authors")
                .entityList(AuthorDTO.class)
                .hasSize(2)
                .path("authors[*].firstName")
                .entityList(String.class)
                .contains("Robert", "Joshua");

        verify(authorService, times(1)).getAllAuthors();
    }
}

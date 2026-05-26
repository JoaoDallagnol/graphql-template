package com.graphql.template.service;

import com.graphql.template.data.Author;
import com.graphql.template.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.getAllAuthors();
    }

    public Author getById(String id) {
        return authorRepository.getById(id);
    }
}
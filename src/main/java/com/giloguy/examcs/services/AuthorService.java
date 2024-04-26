package com.giloguy.examcs.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import java.util.List;
import com.giloguy.examcs.repositories.AuthorRepository;
import com.giloguy.examcs.models.Authors;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public Optional<Authors> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    public Optional<Authors> getAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    public List<Authors> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Authors saveAuthor(Authors author) {
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}

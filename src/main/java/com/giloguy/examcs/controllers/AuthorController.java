package com.giloguy.examcs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import com.giloguy.examcs.models.Authors;
import com.giloguy.examcs.payloads.AuthorRequest;
import com.giloguy.examcs.services.AuthorService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/authors")
@SecurityRequirement(name = "bearerAuth")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Authors> addAuthor(@Valid @RequestBody AuthorRequest author) {
        Authors authorEntity = new Authors(author.getName());
        Authors savedAuthor = authorService.saveAuthor(authorEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAuthor.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedAuthor);
    }

    @GetMapping
    public ResponseEntity<?> getAuthors(@RequestParam(value = "name", required = false) String name) {
        List<Authors> authors;
        Optional<Authors> author;

        if (name != null) {
            author = authorService.getAuthorByName(name);
            return author.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            authors = authorService.getAllAuthors();
            return ResponseEntity.ok().body(authors);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        Optional<Authors> existingAuthor = authorService.getAuthorById(id);

        if (existingAuthor.isPresent()) {
            authorService.deleteAuthor(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}

package com.giloguy.examcs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Optional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.giloguy.examcs.models.Books;
import com.giloguy.examcs.payloads.CreateBookRequest;
import com.giloguy.examcs.services.BookService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
@SecurityRequirement(name = "bearerAuth")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Books> addBook(@Valid @RequestBody CreateBookRequest book) {
        Books savedBook = bookService.saveBook(book);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBook.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable long id) {
        Optional<Books> book = bookService.getBookById(id);

        return book.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Books> updateBook(@RequestBody Books book) {
        Optional<Books> existingBook = bookService.getBookById(book.getId());
        Books updatedBook;

        if (existingBook.isPresent()) {
            updatedBook = bookService.updateBook(book.getId(), book);
            return ResponseEntity.ok().body(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        Optional<Books> existingBook = bookService.getBookById(id);

        if (existingBook.isPresent()) {
            bookService.deleteBook(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

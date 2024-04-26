package com.giloguy.examcs.services;

import com.giloguy.examcs.repositories.BookRepository;
import com.giloguy.examcs.repositories.CategoryRepository;
import com.giloguy.examcs.models.Category;
import com.giloguy.examcs.models.Authors;
import com.giloguy.examcs.models.Books;
import com.giloguy.examcs.payloads.CreateBookRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.giloguy.examcs.repositories.AuthorRepository;

import java.util.Optional;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Optional<Books> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Optional<Books> getBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
    public Books updateBook(Long id, Books book) {
        Books existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook != null) {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthors(book.getAuthors());
            existingBook.setCategories(book.getCategories());
            return bookRepository.save(existingBook);
        } else {
            return null;
        }
    }


    public Page<Books> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    public Books saveBook(CreateBookRequest book) {
        Books newBook = new Books();
        newBook.setTitle(book.getTitle());
        newBook.setDescription(book.getDescription());

        book.getAuthors().forEach(authorId -> {
            Optional<Authors> author = authorRepository.findById(authorId);

            author.ifPresent(value -> newBook.getAuthors().add(value));
        });

        book.getCategories().forEach(categoryId -> {
            Optional<Category> category = categoryRepository.findById(categoryId);

            category.ifPresent(value -> newBook.getCategories().add(value));
        });

        newBook.setPrice(book.getPrice());

        return bookRepository.save(newBook);
    }
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

}

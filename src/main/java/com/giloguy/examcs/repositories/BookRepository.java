package com.giloguy.examcs.repositories;

import com.giloguy.examcs.models.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface BookRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByTitle(String title);
    List<Books> findByIdIn(List<Long> bookIds);
}

package com.giloguy.examcs.repositories;

import com.giloguy.examcs.models.Authors;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Authors, Long>{

    Optional<Authors> findByName(String name);
} 
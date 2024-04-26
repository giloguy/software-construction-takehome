package com.giloguy.examcs.repositories;


import com.giloguy.examcs.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{

    Optional<Users> findByEmail(String email);
    Boolean existsByEmail(String email);
    
}

package com.giloguy.examcs.repositories;

import com.giloguy.examcs.models.Roles;  
import com.giloguy.examcs.models.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles, Long>{
    Optional<Roles> findByName(RoleName roleName);
}

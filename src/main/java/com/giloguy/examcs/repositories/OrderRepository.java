package com.giloguy.examcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.giloguy.examcs.models.Orders;
import java.util.Optional;
import java.util.List;
public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<List<Orders>> findOrdersByCustomerId(Long customerId);
}

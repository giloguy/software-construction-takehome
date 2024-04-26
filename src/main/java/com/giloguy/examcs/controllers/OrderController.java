package com.giloguy.examcs.controllers;

import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.giloguy.examcs.models.Orders;
import com.giloguy.examcs.models.OrderStatus;
import com.giloguy.examcs.services.OrderService;
import com.giloguy.examcs.payloads.CreateOrderRequest;
import com.giloguy.examcs.payloads.CreateOrderDTO;
import com.giloguy.examcs.security.UserPrincipal;
import com.giloguy.examcs.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Orders> addOrder(@CurrentUser UserPrincipal currentUser, @RequestBody CreateOrderRequest order) {
        System.out.println(currentUser);
        Orders savedOrder = orderService.save(currentUser, order);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedOrder.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedOrder);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Orders>> getAllOrders() {
        List<Orders> orders = orderService.findAllOrders();

        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDTO orderDTO, @CurrentUser UserPrincipal currentUser) {
        try {
            Orders createdOrder = orderService.createOrder(currentUser, orderDTO);
            return ResponseEntity.ok(createdOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable Long userId) {
        try {
            List<Orders> userOrders = orderService.findOrdersByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("No orders found for user with id: " + userId));
            return ResponseEntity.ok(userOrders);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatus newStatus) {
        try {
            Orders order = orderService.findOrderById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
            order.setOrderStatus(newStatus);
            Orders updatedOrder = orderService.updateOrder(order);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable long id) {
        Optional<Orders> order = orderService.findOrderById(id);

        if (order.isPresent()) {
            orderService.deleteOrderById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/details/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        try {
            Orders order = orderService.findOrderById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        
    }

}

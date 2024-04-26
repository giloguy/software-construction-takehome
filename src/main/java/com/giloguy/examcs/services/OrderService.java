package com.giloguy.examcs.services;

import com.giloguy.examcs.models.OrderStatus;
import com.giloguy.examcs.models.Users;
import com.giloguy.examcs.models.Books;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.giloguy.examcs.payloads.CreateOrderDTO;
import org.springframework.stereotype.Service;
import com.giloguy.examcs.security.UserPrincipal;
import com.giloguy.examcs.repositories.OrderRepository;
import com.giloguy.examcs.repositories.UserRepository;
import com.giloguy.examcs.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.giloguy.examcs.payloads.CreateOrderRequest;
import com.giloguy.examcs.models.Orders;
@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    public Orders save(UserPrincipal currentUser, CreateOrderRequest order) {
        Orders newOrder = new Orders();

        List<Long> bookIds = order.getBookIds();
        List<Books> booksList = bookRepository.findByIdIn(bookIds);
        long totalPrice = booksList.stream().mapToLong(Books::getPrice).sum();
        Optional<Users> user = userRepository.findById(currentUser.getId());

        user.ifPresent(newOrder::setCustomer);
        newOrder.setPrice(totalPrice);
        newOrder.setOrderStatus(OrderStatus.ORDER_CREATED);

        return orderRepository.save(newOrder);
    }

    public List<Orders> findAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Orders> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Orders createOrder(UserPrincipal currentUser,CreateOrderDTO orderDTO) {
        Orders order = new Orders();
        List<Books> books = new ArrayList<>();
        Long totalPrice = 0L;
    
        // Fetch the list of book IDs from the DTO
        List<Long> bookIds = orderDTO.getBookIds();
    
        // Iterate over the book IDs, fetch the corresponding books from the database,
        // calculate the total price, and add the books to the order
        for (Long bookId : bookIds) {
            Optional<Books> optionalBook = bookRepository.findById(bookId);
            if (optionalBook.isPresent()) {
                Books book = optionalBook.get();
                books.add(book);
                totalPrice += book.getPrice();
            } else {
                throw new RuntimeException("Book not found with id: " + bookId);
            }
        }
    
        // Set the current user as the customer for the order
        Optional<Users> user = userRepository.findById(currentUser.getId());
        user.orElseThrow(() -> new RuntimeException("User not found with id: " + currentUser.getId()));
        order.setCustomer(user.get());
    
        order.setBooks(books);
        order.setPrice(totalPrice);
    
        // Save the order to the database
        return orderRepository.save(order);
    }
    public Optional<List<Orders>> findOrdersByUserId(Long userId) {
        return orderRepository.findOrdersByCustomerId(userId);
    }

    public Orders updateOrder(Orders order) {
        return orderRepository.save(order);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }
}

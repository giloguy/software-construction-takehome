package com.giloguy.examcs.services;

import java.util.Optional;
import java.util.List;
import com.giloguy.examcs.models.Users;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.giloguy.examcs.repositories.UserRepository;

@Service
public class Userservice {
    @Autowired
    private UserRepository userRepository;

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Users updateUser(Long id, Users user) {
        Users existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }
}

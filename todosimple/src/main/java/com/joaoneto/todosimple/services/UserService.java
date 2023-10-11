package com.joaoneto.todosimple.services;

import com.joaoneto.todosimple.models.User;
import com.joaoneto.todosimple.repositories.TaskRepository;
import com.joaoneto.todosimple.repositories.UserRepository;
import com.joaoneto.todosimple.services.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;


    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() ->  new UserNotFoundException("User not found: " + id));
    }

    @Transactional
    public User create(User user) {
        user.setId(null);
        user = this.userRepository.save(user);
        this.taskRepository.saveAll(user.getTasks());
        return user;
    }

    @Transactional
    public User update(User user) {
        User newUser = this.findById(user.getId());
        newUser.setPassword(user.getPassword());
        return this.userRepository.save(newUser);
    }

    @Transactional
    public void delete(Long id) {
        this.findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }


}
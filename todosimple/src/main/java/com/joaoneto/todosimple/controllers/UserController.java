package com.joaoneto.todosimple.controllers;

import com.joaoneto.todosimple.models.User;
import com.joaoneto.todosimple.services.UserService;
import com.joaoneto.todosimple.util.CreateUser;
import com.joaoneto.todosimple.util.UpdateUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/create")
    @Validated(CreateUser.class)
    public ResponseEntity<Void> createUser(@Valid @RequestBody User user) {
        user = this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated(UpdateUser.class)
    public ResponseEntity<Void> updateUser(@Valid @RequestBody User user, @PathVariable Long id) {
        user.setId(id);
        user = this.userService.update(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

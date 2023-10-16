package com.joaoneto.todosimple.controllers;

import com.joaoneto.todosimple.models.Task;
import com.joaoneto.todosimple.services.TaskService;
import com.joaoneto.todosimple.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@Validated
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @RequestMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        Task task = taskService.findById(id);
        return ResponseEntity.ok().body(task);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> findAllTasks() {
        List<Task> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> findByUserId(@PathVariable Long userId) {
        this.userService.findById(userId); // check if user exists (throws exception if not)
        List<Task> tasks = taskService.findByUserId(userId);
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping("/create")
    @Validated
    public ResponseEntity<Void> createTask(@Valid @RequestBody Task task) {
        task = taskService.create(task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> updateTask(@Valid @RequestBody Task task, @PathVariable Long id) {
        task.setId(id);
        task = taskService.update(task);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

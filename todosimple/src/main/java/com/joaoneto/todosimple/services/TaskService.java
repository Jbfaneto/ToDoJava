package com.joaoneto.todosimple.services;

import com.joaoneto.todosimple.models.Task;
import com.joaoneto.todosimple.models.User;
import com.joaoneto.todosimple.repositories.TaskRepository;
import com.joaoneto.todosimple.services.exceptions.TaskNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new TaskNotFoundException("Task not found: " + id));
    }

    @Transactional
    public Task create(Task task) {
        User user = this.userService.findById(task.getUser().getId());
        task.setId(null);
        task.setUser(user);
        task = this.taskRepository.save(task);
        return task;
    }
    @Transactional
    public Task update(Task task){
        Task newTask = this.findById(task.getId());
        newTask.setDescription(task.getDescription());
        return this.taskRepository.save(newTask);
    }

    @Transactional
    public void delete(Long id) {
        this.findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting task: " + e.getMessage());
        }
    }
}

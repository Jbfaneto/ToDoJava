package com.joaoneto.todosimple.services;

import com.joaoneto.todosimple.models.Task;
import com.joaoneto.todosimple.models.User;
import com.joaoneto.todosimple.repositories.TaskRepository;
import com.joaoneto.todosimple.services.exceptions.DataBindingViolationException;
import com.joaoneto.todosimple.services.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public List<Task> findAll(){
        return this.taskRepository.findAll();
    }

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new ObjectNotFoundException("Task not found: " + id));
    }

    public List<Task> findByUserId(Long id) {
        return this.taskRepository.findByUser_Id(id);
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
            throw new DataBindingViolationException("Error deleting task: " + e.getMessage());
        }
    }
}

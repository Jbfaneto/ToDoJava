package com.joaoneto.todosimple.services;

import com.joaoneto.todosimple.models.Task;
import com.joaoneto.todosimple.models.User;
import com.joaoneto.todosimple.models.enums.ProfileEnum;
import com.joaoneto.todosimple.repositories.TaskRepository;
import com.joaoneto.todosimple.security.UserSpringSecurity;
import com.joaoneto.todosimple.services.exceptions.AuthorizationException;
import com.joaoneto.todosimple.services.exceptions.DataBindingViolationException;
import com.joaoneto.todosimple.services.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;


    public Task findById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Task not found: " + id));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task)) {
            throw new AuthorizationException("Access denied");
        }

        return task;
    }

    public List<Task> findByUser() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Access denied");
        }
        List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    @Transactional
    public Task create(Task task) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Access denied");
        }
        User user = this.userService.findById(userSpringSecurity.getId());
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


    public void delete(Long id) {
        this.findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Error deleting task: " + e.getMessage());
        }
    }
    private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return userSpringSecurity.getId().equals(task.getUser().getId());
    }
}

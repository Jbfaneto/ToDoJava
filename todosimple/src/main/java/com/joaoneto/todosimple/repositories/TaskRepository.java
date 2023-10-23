package com.joaoneto.todosimple.repositories;

import com.joaoneto.todosimple.models.Task;
import com.joaoneto.todosimple.models.projections.TaskProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<TaskProjection> findByUser_Id(Long id);

   // @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
    //List<Task> findByUserId(@Param("id") Long id);

}

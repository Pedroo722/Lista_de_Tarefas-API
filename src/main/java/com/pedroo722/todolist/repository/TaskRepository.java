package com.pedroo722.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pedroo722.todolist.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT MAX(t.ordemApresentacao) FROM Task t")
    Integer findMaxOrdemApresentacao();
}

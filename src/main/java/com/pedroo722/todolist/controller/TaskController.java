package com.pedroo722.todolist.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedroo722.todolist.entity.Task;
import com.pedroo722.todolist.service.TaskService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id) {
        return taskService.getTaskById(id)
                          .map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        if (taskService.findTaskByNomeTarefa(task.getNomeTarefa()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error: Uma tarefa com esse nome já existe.");
        }

        if (task.getOrdemApresentacao() == null) {
            Integer nextOrder = taskService.getNextOrder();
            task.setOrdemApresentacao(nextOrder);
        }

        Task savedTask = taskService.saveTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody Task task) {
        if (!taskService.getTaskById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Task> existingTaskWithName = taskService.findTaskByNomeTarefa(task.getNomeTarefa());
        if (existingTaskWithName.isPresent() && !existingTaskWithName.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error: Uma tarefa com esse nome já existe.");
        }

        task.setId(id);
        Task updatedTask = taskService.saveTask(task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        if (taskService.getTaskById(id).isPresent()) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/reorder")
    public ResponseEntity<String> reorderTasks(@RequestBody List<Task> tasks) {
        try {
            taskService.reorderTasks(tasks);
            return ResponseEntity.status(HttpStatus.OK).body("Ordem de tarefas atualizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao atualizar ordem de tarefas.");
        }
    }
}

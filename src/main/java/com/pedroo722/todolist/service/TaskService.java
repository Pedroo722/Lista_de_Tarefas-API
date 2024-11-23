package com.pedroo722.todolist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedroo722.todolist.entity.Task;
import com.pedroo722.todolist.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Integer getNextOrder() {
        Integer maxOrder = taskRepository.findMaxOrdemApresentacao();
        return (maxOrder == null ? 1 : maxOrder + 1);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Optional<Task> findTaskByNomeTarefa(String nomeTarefa) {
        return taskRepository.findAll().stream()
                             .filter(task -> task.getNomeTarefa().equals(nomeTarefa))
                             .findFirst();
    }

    public void reorderTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            // Atualiza a ordem de cada tarefa conforme o índice na lista
            Task task = tasks.get(i);
            task.setOrdemApresentacao(i + 1); // Define a ordem conforme o índice (início em 1)
            taskRepository.save(task); // Persiste a tarefa com a nova ordem
        }
    }
}
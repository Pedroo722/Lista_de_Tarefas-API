package com.pedroo722.todolist.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.pedroo722.todolist.entity.Task;
import com.pedroo722.todolist.repository.TaskRepository;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTasks() {
        // Task de Exemplo
        Task task1 = new Task();
        task1.setId(1L);
        task1.setNomeTarefa("Task 1");
        task1.setCusto(100.0);
        task1.setDataLimite(LocalDate.of(2024, 12, 31));
        task1.setOrdemApresentacao(1);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setNomeTarefa("Task 2");
        task2.setCusto(150.0);
        task2.setDataLimite(LocalDate.of(2024, 11, 30));
        task2.setOrdemApresentacao(2);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        // Validação
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getNomeTarefa());
        assertEquals("Task 2", tasks.get(1).getNomeTarefa());
    }

    @Test
    public void testGetTaskById_Found() {
        // Task de Exemplo
        Task task = new Task();
        task.setId(1L);
        task.setNomeTarefa("Task 1");
        task.setCusto(100.0);
        task.setDataLimite(LocalDate.of(2024, 12, 31));
        task.setOrdemApresentacao(1);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        // Validações
        assertTrue(result.isPresent());
        assertEquals("Task 1", result.get().getNomeTarefa());
    }

    @Test
    public void testGetTaskById_NotFound() {
        // Task de Exemplo
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Task> result = taskService.getTaskById(999L);

        // Validações
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetNextOrder() {
        // Task de Exemplo
        when(taskRepository.findMaxOrdemApresentacao()).thenReturn(5);

        Integer nextOrder = taskService.getNextOrder();

        // Validações
        assertEquals(6, nextOrder);
    }

    @Test
    public void testGetNextOrder_WhenNoTasks() {
        // Task de Exemplo
        when(taskRepository.findMaxOrdemApresentacao()).thenReturn(null);

        Integer nextOrder = taskService.getNextOrder();

        // Validações
        assertEquals(1, nextOrder);
    }

    @Test
    public void testSaveTask() {
        // Task de Exemplo
        Task task = new Task();
        task.setNomeTarefa("New Task");
        task.setCusto(120.0);
        task.setDataLimite(LocalDate.of(2024, 12, 25));
        task.setOrdemApresentacao(1);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setNomeTarefa("New Task");
        savedTask.setCusto(120.0);
        savedTask.setDataLimite(LocalDate.of(2024, 12, 25));
        savedTask.setOrdemApresentacao(1);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        Task result = taskService.saveTask(task);

        // Validações
        assertNotNull(result);
        assertEquals("New Task", result.getNomeTarefa());
        assertEquals(120.0, result.getCusto());
        assertEquals(LocalDate.of(2024, 12, 25), result.getDataLimite());
        assertEquals(1, result.getOrdemApresentacao());
    }

    @Test
    public void testDeleteTask() {
        // Task de Exemplo
        doNothing().when(taskRepository).deleteById(anyLong());

        taskService.deleteTask(1L);

        // Validações
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindTaskByNomeTarefa_Found() {
        // Task de Exemplo
        Task task = new Task();
        task.setId(1L);
        task.setNomeTarefa("Task 1");
        task.setCusto(100.0);
        task.setDataLimite(LocalDate.of(2024, 12, 31));
        task.setOrdemApresentacao(1);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        Optional<Task> result = taskService.findTaskByNomeTarefa("Task 1");

        // Validações
        assertTrue(result.isPresent());
        assertEquals("Task 1", result.get().getNomeTarefa());
    }

    @Test
    public void testFindTaskByNomeTarefa_NotFound() {
        // Task de Exemplo
        when(taskRepository.findAll()).thenReturn(Arrays.asList());

        Optional<Task> result = taskService.findTaskByNomeTarefa("Nonexistent Task");

        // Validações
        assertFalse(result.isPresent());
    }
}


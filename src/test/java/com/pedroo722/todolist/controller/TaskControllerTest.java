package com.pedroo722.todolist.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pedroo722.todolist.entity.Task;
import com.pedroo722.todolist.service.TaskService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void testGetAllTasks() throws Exception {
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
        
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        // Validações
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeTarefa").value("Task 1"))
                .andExpect(jsonPath("$[0].custo").value(100.0))
                .andExpect(jsonPath("$[0].dataLimite").isArray())
                .andExpect(jsonPath("$[0].dataLimite[0]").value(2024))
                .andExpect(jsonPath("$[0].dataLimite[1]").value(12))
                .andExpect(jsonPath("$[0].dataLimite[2]").value(31))
                .andExpect(jsonPath("$[0].ordemApresentacao").value(1))
                .andExpect(jsonPath("$[1].nomeTarefa").value("Task 2"))
                .andExpect(jsonPath("$[1].custo").value(150.0))
                .andExpect(jsonPath("$[1].dataLimite").isArray())
                .andExpect(jsonPath("$[1].dataLimite[0]").value(2024))
                .andExpect(jsonPath("$[1].dataLimite[1]").value(11))
                .andExpect(jsonPath("$[1].dataLimite[2]").value(30))
                .andExpect(jsonPath("$[1].ordemApresentacao").value(2));
    }

    @Test
    public void testGetTaskById_Found() throws Exception {
        // Task de Exemplo
        Task task = new Task();
        task.setId(1L);
        task.setNomeTarefa("Task 1");
        task.setCusto(100.0);
        task.setDataLimite(LocalDate.of(2024, 12, 31));
        task.setOrdemApresentacao(1);
        
        when(taskService.getTaskById(anyLong())).thenReturn(Optional.of(task));

        // Validações
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTarefa").value("Task 1"))
                .andExpect(jsonPath("$.custo").value(100.0))
                .andExpect(jsonPath("$.dataLimite").isArray())
                .andExpect(jsonPath("$.dataLimite[0]").value(2024))
                .andExpect(jsonPath("$.dataLimite[1]").value(12))
                .andExpect(jsonPath("$.dataLimite[2]").value(31))
                .andExpect(jsonPath("$.ordemApresentacao").value(1));
    }

    @Test
    public void testGetTaskById_NotFound() throws Exception {
        // Task de Exemplo
        when(taskService.getTaskById(anyLong())).thenReturn(Optional.empty());

        // Validações
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTask_Success() throws Exception {
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
        
        when(taskService.findTaskByNomeTarefa("New Task")).thenReturn(Optional.empty());
        when(taskService.saveTask(any(Task.class))).thenReturn(savedTask);

        // Validações
        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content("{\"nomeTarefa\":\"New Task\",\"custo\":120.0,\"dataLimite\":\"2024-12-25\",\"ordemApresentacao\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeTarefa").value("New Task"))
                .andExpect(jsonPath("$.custo").value(120.0))
                .andExpect(jsonPath("$.dataLimite").isArray())
                .andExpect(jsonPath("$.dataLimite[0]").value(2024))
                .andExpect(jsonPath("$.dataLimite[1]").value(12))
                .andExpect(jsonPath("$.dataLimite[2]").value(25))
                .andExpect(jsonPath("$.ordemApresentacao").value(1));
    }

    @Test
    public void testCreateTask_BadRequest_DuplicateName() throws Exception {
        // Task de Exemplo
        Task task = new Task();
        task.setNomeTarefa("Existing Task");
        task.setCusto(100.0);
        task.setDataLimite(LocalDate.of(2024, 12, 31));
        task.setOrdemApresentacao(1);
        
        when(taskService.findTaskByNomeTarefa("Existing Task")).thenReturn(Optional.of(new Task()));

        // Validações
        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content("{\"nomeTarefa\":\"Existing Task\",\"custo\":100.0,\"dataLimite\":\"2024-12-31\",\"ordemApresentacao\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Uma tarefa com esse nome já existe."));
    }

    @Test
    public void testUpdateTask_Success() throws Exception {
        // Task de Exemplo
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setNomeTarefa("Task 1");
        existingTask.setCusto(100.0);
        existingTask.setDataLimite(LocalDate.of(2024, 12, 31));
        existingTask.setOrdemApresentacao(1);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setNomeTarefa("Updated Task");
        updatedTask.setCusto(150.0);
        updatedTask.setDataLimite(LocalDate.of(2024, 11, 30));
        updatedTask.setOrdemApresentacao(1);
        
        when(taskService.getTaskById(anyLong())).thenReturn(Optional.of(existingTask));
        when(taskService.saveTask(any(Task.class))).thenReturn(updatedTask);

        // Validações
        mockMvc.perform(put("/api/tasks/1")
                        .contentType("application/json")
                        .content("{\"nomeTarefa\":\"Updated Task\",\"custo\":150.0,\"dataLimite\":\"2024-11-30\",\"ordemApresentacao\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTarefa").value("Updated Task"))
                .andExpect(jsonPath("$.custo").value(150.0))
                .andExpect(jsonPath("$.dataLimite").isArray())
                .andExpect(jsonPath("$.dataLimite[0]").value(2024))
                .andExpect(jsonPath("$.dataLimite[1]").value(11))
                .andExpect(jsonPath("$.dataLimite[2]").value(30))
                .andExpect(jsonPath("$.ordemApresentacao").value(1));
    }

    @Test
    public void testUpdateTask_NotFound() throws Exception {
        // Task de Exemplo
        when(taskService.getTaskById(anyLong())).thenReturn(Optional.empty());

        // Validações
        mockMvc.perform(put("/api/tasks/999")
                        .contentType("application/json")
                        .content("{\"nomeTarefa\":\"Updated Task\",\"custo\":150.0,\"dataLimite\":\"2024-11-30\",\"ordemApresentacao\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        // Task de Exemplo
        when(taskService.getTaskById(anyLong())).thenReturn(Optional.of(new Task()));

        // Validações
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteTask_NotFound() throws Exception {
        // Task de Exemplo
        when(taskService.getTaskById(anyLong())).thenReturn(Optional.empty());

        // Validações
        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }
}
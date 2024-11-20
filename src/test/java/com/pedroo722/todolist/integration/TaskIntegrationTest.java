package com.pedroo722.todolist.integration;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pedroo722.todolist.controller.TaskController;
import com.pedroo722.todolist.entity.Task;
import com.pedroo722.todolist.repository.TaskRepository;

@SpringBootTest
public class TaskIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // Inicializa o MockMVC e limpa o repositorio a cada teste
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        taskRepository.deleteAll();
    }

    @Test
    public void testCreateTask() throws Exception {
        String taskJson = "{\"nomeTarefa\":\"Test Task\",\"custo\":100.0,\"dataLimite\":\"2024-12-31\",\"ordemApresentacao\":1}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                .contentType("application/json")
                .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeTarefa").value("Test Task"))
                .andExpect(jsonPath("$.custo").value(100.0))
                .andExpect(jsonPath("$.dataLimite").isArray())
                .andExpect(jsonPath("$.dataLimite[0]").value(2024))
                .andExpect(jsonPath("$.dataLimite[1]").value(12))
                .andExpect(jsonPath("$.dataLimite[2]").value(31))
                .andExpect(jsonPath("$.ordemApresentacao").value(1));
    }

    @Test
    public void testCreateTaskWithDuplicateName() throws Exception {
        // Task de exmeplo
        String taskJson1 = "{\"nomeTarefa\":\"Duplicate Task\",\"custo\":100.0,\"dataLimite\":\"2024-12-31\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                .contentType("application/json")
                .content(taskJson1))
                .andExpect(status().isCreated());

        // Tenta criar task com mesmo nome
        String taskJson2 = "{\"nomeTarefa\":\"Duplicate Task\",\"custo\":150.0,\"dataLimite\":\"2024-12-31\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                .contentType("application/json")
                .content(taskJson2))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Uma tarefa com esse nome já existe."));
    }

    @Test
    public void testGetTaskById() throws Exception {
        Task task = new Task();
        task.setNomeTarefa("Task to Retrieve");
        task.setCusto(50.0);
        task.setDataLimite(LocalDate.of(2024, 12, 31));
        task.setOrdemApresentacao(2);
        taskRepository.save(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTarefa").value("Task to Retrieve"))
                .andExpect(jsonPath("$.custo").value(50.0))
                .andExpect(jsonPath("$.dataLimite").isArray())
                .andExpect(jsonPath("$.dataLimite[0]").value(2024))
                .andExpect(jsonPath("$.dataLimite[1]").value(12))
                .andExpect(jsonPath("$.dataLimite[2]").value(31))
                .andExpect(jsonPath("$.ordemApresentacao").value(2));
    }

    @Test
    public void testUpdateTask() throws Exception {
        Task task = new Task();
        task.setNomeTarefa("Old Task");
        task.setCusto(100.0);
        task.setDataLimite(LocalDate.of(2024, 12, 31));
        task.setOrdemApresentacao(3);
        task = taskRepository.save(task);

        String updatedTaskJson = "{\"nomeTarefa\":\"Updated Task\",\"custo\":120.0,\"dataLimite\":\"2024-12-31\",\"ordemApresentacao\":3}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{id}", task.getId())
                .contentType("application/json")
                .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTarefa").value("Updated Task"))
                .andExpect(jsonPath("$.custo").value(120.0))
                .andExpect(jsonPath("$.dataLimite").isArray())
                .andExpect(jsonPath("$.dataLimite[0]").value(2024))
                .andExpect(jsonPath("$.dataLimite[1]").value(12))
                .andExpect(jsonPath("$.dataLimite[2]").value(31));
    }

    @Test
    public void testDeleteTask() throws Exception {
        Task task = new Task();
        task.setNomeTarefa("Task to Delete");
        task.setCusto(50.0);
        task.setDataLimite(LocalDate.of(2024, 12, 31));
        task.setOrdemApresentacao(4);
        task = taskRepository.save(task);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/{id}", task.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}", task.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Task task1 = new Task();
        task1.setNomeTarefa("Task 1");
        task1.setCusto(200.0);
        task1.setDataLimite(LocalDate.of(2024, 12, 31));
        task1.setOrdemApresentacao(1);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setNomeTarefa("Task 2");
        task2.setCusto(300.0);
        task2.setDataLimite(LocalDate.of(2024, 12, 31));
        task2.setOrdemApresentacao(2);
        taskRepository.save(task2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeTarefa").value("Task 1"))
                .andExpect(jsonPath("$[1].nomeTarefa").value("Task 2"));
    }
}
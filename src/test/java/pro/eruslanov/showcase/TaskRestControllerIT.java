package pro.eruslanov.showcase;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class TaskRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    InMemTaskRepository taskRepository;

    @AfterEach
    void tearDown() {
        this.taskRepository.getTasks().clear();
    }

    @Test
    void handleGetAllTasks_ReturnsValidResponseEntity() throws Exception {
        // given
        var requestBuilder = get("/api/v1/tasks");
        this.taskRepository.getTasks()
                .addAll(List.of(new Task(UUID.fromString("96ef3ea2-9ef1-11ed-ad46-00155d174057"),
                                "first task", false),
                        new Task(UUID.fromString("9946a352-9ef1-11ed-9c87-00155d174057"),
                                "second task", true)));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id": "96ef3ea2-9ef1-11ed-ad46-00155d174057",
                                        "details": "first task",
                                        "completed": false                                                                                               
                                    },
                                    {
                                        "id": "9946a352-9ef1-11ed-9c87-00155d174057",
                                        "details": "second task",
                                        "completed": true                                                                 
                                    }
                                ]
                                """)
                );
    }

    @Test
    void handleCreateNewTask_PayloadIsValid_ReturnsValidResponseEntity() throws Exception {
        // given
        var requestBuilder = post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "details": "third task"
                        }
                        """);

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isCreated(),
                        header().exists(HttpHeaders.LOCATION),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "details": "third task",
                                    "completed": false
                                }
                                """),
                        jsonPath("$.id").exists()
                        );
        assertEquals(1, this.taskRepository.getTasks().size());
        final var task = this.taskRepository.getTasks().get(0);
        assertNotNull(task.id());
        assertEquals("third task", task.details());
        assertFalse(task.completed());
    }

    @Test
    void handleCreateNewTask_PayloadIsInvalid_ReturnsValidResponseEntity() throws Exception {
        // given
        var requestBuilder = post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .content("""
                        {
                            "details": null
                        }
                        """);

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isBadRequest(),
                        header().doesNotExist(HttpHeaders.LOCATION),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "errors": ["Task details mast be set"]
                                }
                                """, true)
                );
        assertTrue(this.taskRepository.getTasks().isEmpty());
    }

}
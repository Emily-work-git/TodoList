package com.example.TodoList.controller;

import com.example.TodoList.model.Todo;
import com.example.TodoList.repository.TodoRepository;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc client;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private JacksonTester<List<Todo>> todoListJacksonTester;

    @BeforeEach
    void setup(){
        todoRepository.deleteAll();
        todoRepository.flush();
        todoRepository.save(new Todo("todo 1", false));
        todoRepository.save(new Todo("todo 2", false));
        todoRepository.save(new Todo("todo 3", false));
    }

    @Test
    void should_return_all_todo() throws Exception {
        final List<Todo> givenTodo = todoRepository.findAll();

        final MvcResult result = client.perform(MockMvcRequestBuilders.get("/todo")).andReturn();
        final MockHttpServletResponse response = result.getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        final List<Todo> fetchedTodo = todoListJacksonTester.parseObject(response.getContentAsString());
        assertThat(fetchedTodo).hasSameSizeAs(givenTodo);
        assertThat(fetchedTodo)
                .usingRecursiveComparison(
                        RecursiveComparisonConfiguration.builder()
                                .withComparedFields("text", "done")
                                .build()
                )
                .isEqualTo(givenTodo);
    }

    @Test
    void should_return_created_todo() throws Exception {
        //Given
        String givenText = "todo5";
        boolean done = false;
        String requestBody = "{\"text\": \"" + givenText + "\", \n\n\"done\": " + done + " }";
        // When
        // Then
        client.perform(
                        MockMvcRequestBuilders.post("/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(givenText))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(done));
    }

}

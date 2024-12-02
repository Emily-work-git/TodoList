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
import static org.assertj.core.api.InstanceOfAssertFactories.atomicIntegerFieldUpdater;

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
        String requestBody = String.format("{\"text\": \"%s\", \"done\": \"%b\"}",givenText,done);
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

    @Test
    void should_return_updated_todo_when_update_with_id_and_data() throws Exception {
        //Given
        final List<Todo> givenTodo = todoRepository.findAll();
        Integer updateId = givenTodo.get(0).getId();
        String newText = "new text";
        boolean newDone = true;
        String requestBody = String.format("{\"id\": %s, \"text\": \"%s\", \"done\": \"%b\"}", updateId,newText,newDone);

        //When
        //Then
        client.perform(
                        MockMvcRequestBuilders.put("/todo/"+updateId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updateId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(newText))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(newDone));
    }

    @Test
    void should_return_no_content_when_delete() throws Exception {
        // Given
        final List<Todo> givenTodo = todoRepository.findAll();
        Integer toDeleteTodoId = givenTodo.get(0).getId();
        // When
        final var result =
                client.perform(MockMvcRequestBuilders.delete("/todo/" + toDeleteTodoId)).andReturn();

        // Then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(todoRepository.findAll()).hasSize(2);
    }


}

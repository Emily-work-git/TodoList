package com.example.TodoList.controller;

import com.example.TodoList.model.Todo;
import com.example.TodoList.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.TodoList.exception.TodoNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAllTodoItems(){
        return todoService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody Todo todo){
        return todoService.create(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Integer id, @RequestBody Todo todo) throws TodoNotFoundException {
        return todoService.update(id, todo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Integer id) throws TodoNotFoundException {
        todoService.delete(id);
    }
}

package com.example.TodoList.controller;

import com.example.TodoList.model.Todo;
import com.example.TodoList.service.TodoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/todo")
@CrossOrigin(origins = "http://localhost:3000")


public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAllTodoItems(){
        return todoService.findAll();
    }


}

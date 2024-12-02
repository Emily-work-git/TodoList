package com.example.TodoList.service;

import com.example.TodoList.model.Todo;
import com.example.TodoList.repository.TodoRepository;
import org.springframework.stereotype.Service;
import com.example.TodoList.exception.TodoNotFoundException;
import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll(){
        return todoRepository.findAll();
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(Integer id, Todo todo) {
        Todo toUpdateTodo = todoRepository.findById(id).orElseThrow(TodoNotFoundException::new);
        toUpdateTodo.setText(todo.getText());
        toUpdateTodo.setDone(todo.isDone());
        return todoRepository.save(toUpdateTodo);
    }

    public void delete(Integer id) {
        todoRepository.deleteById(id);
    }
}

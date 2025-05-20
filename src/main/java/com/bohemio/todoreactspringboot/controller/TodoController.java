package com.bohemio.todoreactspringboot.controller;

import com.bohemio.todoreactspringboot.entity.Todo;
import com.bohemio.todoreactspringboot.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }



    @GetMapping("/users/{username}/todos")
    public List<Todo> retrieveTodos(@PathVariable String username) {
        return todoService.findByUsername(username);
    }

    @DeleteMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable String username,
            @PathVariable Long id) {

        boolean deleted = todoService.deleteById(id);


        if(deleted){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Todo> retrieveTodo(
            @PathVariable String username,
            @PathVariable Long id) {

        Optional<Todo> todoOptional = todoService.findById(id);

        if (todoOptional.isPresent()) {
            return ResponseEntity.ok(todoOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("users/{username}/todos")
    public ResponseEntity<Todo> createTodo(@PathVariable String username, @Valid @RequestBody Todo todo) {
        todo.setUsername(username);
        Todo savedTodo = todoService.save(todo);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTodo.getId())
                .toUri();


        return ResponseEntity.created(location).body(savedTodo);
    }

    @PutMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Todo> updateTodo(
            @PathVariable String username,
            @PathVariable long id,
            @Valid @RequestBody Todo todo) { // 요청 본문의 JSON 데이터를 Todo 객체로 변환

        // 경로 변수의 username과 id를 Todo 객체에 일관되게 설정
        todo.setUsername(username);
        todo.setId(id); // ★★★ 경로의 id를 사용해서 업데이트할 대상을 명확히 함

        Todo updatedTodo = todoService.save(todo); // 이 save는 이제 수정 로직을 탈 수 있음

        if (updatedTodo != null) {
            return ResponseEntity.ok(updatedTodo); // 200 OK 와 함께 수정된 Todo 객체 반환
        } else {
            return ResponseEntity.notFound().build(); // 수정할 대상을 찾지 못한 경우 404 Not Found
        }
    }

}

package com.bohemio.todoreactspringboot.controller;

import com.bohemio.todoreactspringboot.entity.Todo;
import com.bohemio.todoreactspringboot.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
            @PathVariable int id) {

        boolean deleted = todoService.deleteById(id); // 서비스 계층에 삭제 요청


        if(deleted){
            return ResponseEntity.noContent().build(); // 204 No Content (삭제 성공)
        } else {
            return ResponseEntity.notFound().build();// 404 Not Found (해당 ID 없음)
        }

        // 삭제 성공 시 204 No Content 응답 반환
        return ResponseEntity.noContent().build();
        // 또는 단순히 ResponseEntity.ok().build(); 로 200 OK를 반환할 수도 있습니다.
        // 204 No Content는 "성공적으로 처리했지만, 반환할 콘텐츠는 없음"을 의미합니다.
    }

    @GetMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Todo> retrieveTodo(
            @PathVariable String username,
            @PathVariable int id) {

        Optional<Todo> todoOptional = todoService.findById(id);

        if (todoOptional.isPresent()) {
            return ResponseEntity.ok(todoOptional.get()); // 200 OK 와 함께 Todo 데이터 반환
        } else {
            return ResponseEntity.notFound().build();     // 404 Not Found 반환
        }
        // 참고: 현재 username은 경로에는 있지만, findById에서는 사용하지 않고 있습니다.
        // 나중에 DB 연동 시 "해당 사용자가 이 할 일의 소유주인가?" 검증 로직 추가 가능
    }

}

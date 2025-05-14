package com.bohemio.todoreactspringboot.service;

import com.bohemio.todoreactspringboot.entity.Todo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaticListTodoServiceImpl implements TodoService {

    private static List<Todo> todos = new ArrayList<>();
    private static int todoscount = 0;

    static {
        todos.add(new Todo(++todoscount, "user1", "AWS 인터페이스로 공부하기", LocalDate.now().plusYears(1), false));
        todos.add(new Todo(++todoscount, "user2", "React 인터페이스로 마스터하기", LocalDate.now().plusYears(2), false));
        todos.add(new Todo(++todoscount, "user3", "댄스 인터페이스로 배우기", LocalDate.now().plusMonths(6), false));
    }

    @Override
    public List<Todo> findAll() {
        return todos;
    }

    @Override
    public List<Todo> findByUsername(String username) {
        return todos.stream()
                .filter(todo -> todo.getUsername().equalsIgnoreCase(username)) // 대소문자 구분 없이 비교
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(int id) {
        return todos.removeIf( todo -> todo.getId() == id );
    }

    @Override
    public Optional<Todo> findById(int id) {
        return todos.stream().filter( todo -> todo.getId() == id ).findFirst();
    }
}

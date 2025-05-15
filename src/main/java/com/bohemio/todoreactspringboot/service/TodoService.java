package com.bohemio.todoreactspringboot.service;

import com.bohemio.todoreactspringboot.entity.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {

    List<Todo> findAll();

    List<Todo> findByUsername(String username);

    boolean deleteById(Long id);

    Optional<Todo> findById(Long id);

    Todo save(Todo todo);

}

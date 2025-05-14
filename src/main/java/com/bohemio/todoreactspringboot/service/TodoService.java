package com.bohemio.todoreactspringboot.service;

import com.bohemio.todoreactspringboot.entity.Todo;

import java.util.List;

public interface TodoService {

    List<Todo> findAll();

    List<Todo> findByUsername(String username);

    boolean deleteById(int id);

    <Optional>Todo findById(int id);

}

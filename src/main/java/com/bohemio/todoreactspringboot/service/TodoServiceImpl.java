package com.bohemio.todoreactspringboot.service;

import com.bohemio.todoreactspringboot.entity.Todo;
import com.bohemio.todoreactspringboot.repository.TodoRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    private static List<Todo> todos = new ArrayList<>();
    private static long todoscount = 0;

    @Override
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    @PreAuthorize("#username == authentication.name or hasRole('ADMIN')")
    public List<Todo> findByUsername(String username) {
        return todoRepository.findByUsername(username);
    }

    @Override
    public boolean deleteById(Long id) {
        if(todoRepository.existsById(id)){
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    @Override
    public Todo save(Todo todo) {
        // JpaRepository의 save() 메소드는 엔티티에 ID가 없거나 존재하지 않으면 INSERT,
        // ID가 있고 해당 ID의 엔티티가 존재하면 UPDATE를 수행합니다.

        // '수정'의 경우, ID가 존재하지 않는다면 새로운 리소스를 만들지 않고,
        // "수정할 대상을 찾을 수 없음"을 컨트롤러에 알려주는 것이 PUT의 일반적인 의미에 더 맞습니다.
        // (POST는 없으면 새로 생성)
        // 따라서, 만약 todo 객체에 ID가 있는데 (즉, 업데이트 시도인데)
        // 해당 ID가 DB에 존재하지 않으면 null을 반환하여 컨트롤러가 404를 보내도록 합니다.
        if (todo.getId() != null) { // ID가 있다는 것은 업데이트 시도일 가능성
            Optional<Todo> existingTodo = todoRepository.findById(todo.getId());
            if (existingTodo.isEmpty()) { //리소스 없으면 Null반환(404처리)
                return null;
            }
            // ID가 있고, 해당 리소스도 존재하면 JPA save는 UPDATE를 수행합니다.
        }
        // ID가 없거나(새로 생성), ID가 있고 해당 리소스도 존재하면(업데이트)
        // JPA save가 알아서 처리해줍니다.
        return todoRepository.save(todo);
    }
    //

}

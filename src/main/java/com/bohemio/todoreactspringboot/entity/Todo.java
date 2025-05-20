package com.bohemio.todoreactspringboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@SQLDelete(sql = "UPDATE todo SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @NotBlank(message = "할 일 설명은 필수 입력 항목입니다.")
    @Size(max = 255, message = "할 일 설명은 최대 255자까지 가능합니다.")
    private String description;

    @NotNull(message = "목표 날짜는 필수 입력 항목입니다.")
    // @FutureOrPresent(message = "목표 날짜는 오늘 또는 미래의 날짜여야 합니다.") // 필요에 따라 추가
    private LocalDate targetDate;


    private boolean done;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    public Todo() {
    }

    public Todo(Long id, String username, String description, LocalDate targetDate, boolean done, boolean deleted) {
        this.id = id;
        this.username = username;
        this.description = description;
        this.targetDate = targetDate;
        this.done = done;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return id == todo.id && done == todo.done && Objects.equals( username, todo.username ) && Objects.equals( description, todo.description ) && Objects.equals( targetDate, todo.targetDate );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, username, description, targetDate, done );
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", description='" + description + '\'' +
                ", targetDate=" + targetDate +
                ", done=" + done +
                '}';
    }
}

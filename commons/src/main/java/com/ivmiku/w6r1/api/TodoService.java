package com.ivmiku.w6r1.api;

import com.ivmiku.w6r1.entity.Todo;

import java.util.List;

public interface TodoService {
    void insertOne(Todo todo, String userId);

    int changeToPengding(String todoId, String userId);

    int changeToFinish(String todoId, String userId);

    int changeAllToPending(String userId);

    int changeAllToFinish(String userId);

    List<Todo> getAllTodo(String userId, int current, int size);

    int deleteOne(String todoId);

    int deleteAll(String flag, String userId);

    List<Todo> search(String keyword, String flag, int current, int size, String userId);
}

package com.reon.taskservice.service;

import com.reon.taskservice.dto.TaskCreation;
import com.reon.taskservice.dto.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse creatNewTask(TaskCreation taskCreation);
    List<TaskResponse> fetchAllTask();
    TaskResponse updateTask(Long taskId, TaskCreation updateCurrentTask);
    void deleteTask(Long taskId);

    // Fetch tasks for given user
    List<TaskResponse> fetchTasksByUserId(String userId);
}

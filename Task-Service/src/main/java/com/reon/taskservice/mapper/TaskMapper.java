package com.reon.taskservice.mapper;

import com.reon.taskservice.dto.TaskCreation;
import com.reon.taskservice.dto.TaskResponse;
import com.reon.taskservice.model.Task;

import java.time.LocalDate;

public class TaskMapper {
    public static Task mapToTask(TaskCreation taskCreation){
        Task task = new Task();
        task.setTitle(taskCreation.getTitle());
        task.setDescription(taskCreation.getDescription());
        task.setDueDate(taskCreation.getDueDate());
        task.setTaskStatus(taskCreation.getTaskStatus());
        task.setPriority(taskCreation.getPriority());
        task.setCategory(taskCreation.getCategory());
        task.setUserId(taskCreation.getUserId());
        return task;
    }

    public static TaskResponse taskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setDueDate(task.getDueDate());
        response.setTaskStatus(task.getTaskStatus());
        response.setPriority(task.getPriority());
        response.setCategory(task.getCategory());
        response.setCreatedOn(task.getCreatedOn().toLocalDate());
        if (task.getUpdateOn() != null) {
            response.setUpdateOn(task.getUpdateOn().toLocalDate());
        }
        response.setUserId(task.getUserId());
        return response;
    }

    public static void updateTask(Task task, TaskCreation updateTask){
        if (updateTask.getTitle() != null && !updateTask.getTitle().isBlank()){
            task.setTitle(updateTask.getTitle());
        }
        if (updateTask.getDescription() != null && !updateTask.getDescription().isBlank()) {
            task.setDescription(updateTask.getDescription());
        }
        if (updateTask.getDueDate() != null) {
            task.setDueDate(updateTask.getDueDate());
        }
        if (updateTask.getTaskStatus() != null) {
            task.setTaskStatus(updateTask.getTaskStatus());
        }
        if (updateTask.getPriority() != null) {
            task.setPriority(updateTask.getPriority());
        }
        if (updateTask.getCategory() != null) {
            task.setCategory(updateTask.getCategory());
        }
    }
}

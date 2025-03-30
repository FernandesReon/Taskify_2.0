package com.reon.taskservice.service.impl;

import com.reon.taskservice.dto.TaskCreation;
import com.reon.taskservice.dto.TaskResponse;
import com.reon.taskservice.exception.TaskNotFoundException;
import com.reon.taskservice.exception.TitleAlreadyExistsException;
import com.reon.taskservice.mapper.TaskMapper;
import com.reon.taskservice.model.Task;
import com.reon.taskservice.repository.TaskRepository;
import com.reon.taskservice.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TaskServiceImpl implements TaskService {
    private final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse creatNewTask(TaskCreation taskCreation) {
        if (taskRepository.existsByTitle(taskCreation.getTitle())){
            throw new TitleAlreadyExistsException("A task with this title already exists: " + taskCreation.getTitle());
        }
        logger.info("Creating task...");

        Task task = TaskMapper.mapToTask(taskCreation);

        task.setCreatedOn(LocalDateTime.now());
        Task saveTask = taskRepository.save(task);

        logger.info("Task created");
        return TaskMapper.taskResponse(saveTask);
    }

    @Override
    public List<TaskResponse> fetchAllTask() {
        List<Task> allTasks = taskRepository.findAll();
        return allTasks.stream().map(TaskMapper :: taskResponse).toList();
    }

    @Override
    public TaskResponse updateTask(Long taskId, TaskCreation updateCurrentTask) {
        logger.info("Updating task with id: " + taskId);

        Task existingTask = taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException("Task not found with id: " + taskId));
        TaskMapper.updateTask(existingTask, updateCurrentTask);
        existingTask.setUpdateOn(LocalDateTime.now());
        Task saveTask = taskRepository.save(existingTask);

        logger.info("Task updated successfully [id]: " + taskId);
        return TaskMapper.taskResponse(saveTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        logger.info("Deleting task with id: " + taskId);
        taskRepository.deleteById(taskId);
    }
}

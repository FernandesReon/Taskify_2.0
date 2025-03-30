package com.reon.taskservice.controller;

import com.reon.taskservice.dto.TaskCreation;
import com.reon.taskservice.dto.TaskResponse;
import com.reon.taskservice.service.impl.TaskServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createNewTask(@Valid @RequestBody TaskCreation taskCreation){
        logger.info("Task creation controller :: creating task with title: " + taskCreation.getTitle() + " ongoing.");
        TaskResponse newTask = taskService.creatNewTask(taskCreation);
        logger.info("New task created successfully with title: " + taskCreation.getTitle());
        return ResponseEntity.ok().body(newTask);
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<TaskResponse>> fetchAllTasks(){
        List<TaskResponse> taskResponseList = taskService.fetchAllTask();
        return ResponseEntity.ok().body(taskResponseList);
    }
}

package com.reon.taskservice.controller;

import com.reon.taskservice.dto.TaskCreation;
import com.reon.taskservice.dto.TaskResponse;
import com.reon.taskservice.service.impl.TaskServiceImpl;
import com.reon.taskservice.validators.CreateValidatorGroup;
import com.reon.taskservice.validators.UpdateValidatorGroup;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<TaskResponse> createNewTask(
            @Validated({CreateValidatorGroup.class, Default.class}) @RequestBody TaskCreation taskCreation)
    {
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

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateExistingTask(
            @Validated(UpdateValidatorGroup.class) @RequestBody TaskCreation updateTask,
            @PathVariable Long id)
    {
        logger.info("Task updating controller :: updating task with id: " + id + " ongoing.");
        TaskResponse existingTask = taskService.updateTask(id, updateTask);
        logger.info("Update task successfully with id: " + id);
        return ResponseEntity.ok().body(existingTask);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        logger.info("Delete controller :: deleting task with id: " + id);
        taskService.deleteTask(id);
        logger.info("Task deleted with id: " + id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> fetchTasksByUserId(@PathVariable String userId){
        logger.info("Controller :: Fetching tasks for userId: " + userId);
        List<TaskResponse> taskResponseList = taskService.fetchTasksByUserId(userId);
        return ResponseEntity.ok().body(taskResponseList);
    }
}

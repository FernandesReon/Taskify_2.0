package com.reon.userservice.service;

import com.reon.userservice.model.task_ms.TaskResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@FeignClient(name = "task-service", url = "http://localhost:8081/task")
public interface TaskClient {
    @GetMapping("/user/{userId}")
    List<TaskResponse> getTasksByUserId(@PathVariable("userId") String userId);
}
package com.reon.taskservice.dto;

import com.reon.taskservice.model.Category;
import com.reon.taskservice.model.Priority;
import com.reon.taskservice.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String title;
    private String description;
    private LocalDate dueDate;
    private TaskStatus taskStatus;
    private Priority priority;
    private Category category;
    private LocalDate createdOn;
    private LocalDate updateOn;
}

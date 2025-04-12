package com.reon.taskservice.dto;

import com.reon.taskservice.model.Category;
import com.reon.taskservice.model.Priority;
import com.reon.taskservice.model.TaskStatus;
import com.reon.taskservice.validators.CreateValidatorGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreation {

    @NotBlank(message = "Title is required", groups = CreateValidatorGroup.class)
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters.")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private TaskStatus taskStatus = TaskStatus.In_Progress;
    private Priority priority = Priority.MEDIUM;
    private Category category = Category.OTHERS;

    // Add userId to associate the task with a user
    @NotBlank(message = "User ID is required.", groups = CreateValidatorGroup.class)
    private String userId;
}
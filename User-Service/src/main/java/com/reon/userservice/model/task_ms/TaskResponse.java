package com.reon.userservice.model.task_ms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse implements Serializable {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private TaskStatus taskStatus;
    private Priority priority;
    private Category category;
    private LocalDate createdOn;
    private LocalDate updateOn;
    private String userId;
}
package com.sv.todo_server.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTaskRequest {
    @Size(min = 1, max = 255, message = "Title can't be longer than 255 chars")
    private String title;
    private Boolean completed;
}

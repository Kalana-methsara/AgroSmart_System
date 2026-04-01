package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlantingTaskDTO implements Serializable {
    private String taskId;

    @NotBlank(message = "Task name is required")
    private String taskName;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @Pattern(regexp = "PENDING|COMPLETED", message = "Status must be PENDING or COMPLETED")
    private String status;

    @Positive(message = "Duration must be positive")
    private Double calculatedDuration;

    @NotBlank(message = "Field crop ID is required")
    private String fieldCropId;

    private String cropId;
}
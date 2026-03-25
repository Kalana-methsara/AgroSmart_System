package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO implements Serializable {

    private String equipmentId;

    @NotBlank(message = "Equipment name cannot be blank")
    @Size(max = 50, message = "Equipment name must be at most 50 characters")
    private String category;

    @NotBlank(message = "Type cannot be blank")
    @Size(max = 30, message = "Type must be at most 30 characters")
    private String type;

    @NotBlank(message = "Status cannot be blank")
    @Size(max = 20, message = "Status must be at most 20 characters")
    private String status;

    private String fieldId;
    private String staffId;
}
package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogInventoryDTO implements Serializable {

    private String logInventoryId;

    @NotBlank(message = "Log ID cannot be blank")
    private String logId;

    @NotBlank(message = "Inventory ID cannot be blank")
    private String inventoryId;

    @NotNull(message = "Used quantity cannot be null")
    @Positive(message = "Used quantity must be greater than 0")
    private Double usedQuantity;
}
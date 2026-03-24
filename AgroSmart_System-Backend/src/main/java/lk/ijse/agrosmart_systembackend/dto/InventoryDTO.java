package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.ijse.agrosmart_systembackend.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO implements Serializable {

    private String inventoryId;

    @NotBlank(message = "Item name cannot be blank")
    @Size(max = 50, message = "Item name must be at most 50 characters")
    private String itemName;

    @NotBlank(message = "Category cannot be blank")
    @Size(max = 30, message = "Category must be at most 30 characters")
    private Category category;

    @NotNull(message = "Quantity cannot be null")
    private Double quantity;

    @NotBlank(message = "Unit cannot be blank")
    @Size(max = 10, message = "Unit must be at most 10 characters")
    private String unit;

    private LocalDate lastUpdated;
}
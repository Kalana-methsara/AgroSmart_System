package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogDTO implements Serializable {
    private String logId;
    private LocalDate date;
    @NotBlank(message = "Details cannot be blank")
    @Size(max = 255, message = "Details must be at most 255 characters")
    private String details;
    @NotBlank(message = "Temperature cannot be blank")
    @Pattern(regexp = "^-?\\d+(\\.\\d+)?°?[CF]$", message = "Temperature must be a valid format, e.g., 36.5°C or 98°F")
    private String temperature;
    @Size(max = 10485760, message = "Image size exceeds maximum allowed length")
    private String observedImg;
    private Double landSize; // වගා කරන බිම් ප්‍රමාණය (Acres)
    private LocalDate estimatedHarvestDate; // පද්ධතිය ගණනය කරන දිනය
    @NotEmpty(message = "Field ID cannot be empty")
    private String fieldId;
    @NotEmpty(message = "Crop ID cannot be empty")
    private String cropId;
    private List<String> staff;

    private List<InventoryItem> inventory;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class InventoryItem {
        private String inventoryId;
        private Double usedQuantity;
    }
}
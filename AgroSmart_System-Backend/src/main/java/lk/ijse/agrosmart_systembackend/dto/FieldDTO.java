package lk.ijse.agrosmart_systembackend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldDTO implements Serializable {
    private String fieldId;

    @NotBlank(message = "Field name is required")
    private String fieldName;

    @NotBlank(message = "Location name is required")
    private String locationName; // Maps to "location" in frontend

    @NotBlank(message = "Coordinates are required")
    private String coordinates;  // Maps to "6.9497,80.7891"

    @NotNull(message = "Area is required")
    private Double area;         // Maps to 78.2

    @NotBlank(message = "Area unit is required")
    private String areaUnit;     // Maps to "acres"

    private String description;

    @Column(columnDefinition = "TEXT")
    private String image;
}
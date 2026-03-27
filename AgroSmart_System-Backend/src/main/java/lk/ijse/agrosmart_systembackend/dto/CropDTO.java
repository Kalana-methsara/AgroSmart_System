package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropDTO implements Serializable {

    private String cropId;

    @NotBlank(message = "Common name cannot be blank")
    @Size(max = 100, message = "Common name must be at most 100 characters")
    private String commonName;

    @NotBlank(message = "Scientific name cannot be blank")
    @Size(max = 150, message = "Scientific name must be at most 150 characters")
    private String scientificName;

    @NotBlank(message = "Category cannot be blank")
    @Size(max = 50, message = "Category must be at most 50 characters")
    private String category;

    @NotBlank(message = "Season cannot be blank")
    @Size(max = 50, message = "Season must be at most 50 characters")
    private String season;

    private String cropImg;

    @Positive(message = "Growth duration must be a positive number")
    private Integer growthDurationDays;

    @PositiveOrZero(message = "Fertilizer start day cannot be negative")
    private Integer fertilizerStartDay;

    @Positive(message = "Fertilizer interval must be at least 1 day")
    private Integer fertilizerIntervalDays;

    @Min(value = 1, message = "Water index must be at least 1")
    @Max(value = 10, message = "Water index cannot exceed 10")
    private Integer waterRequirementIndex;

    private Double minTemp;
    private Double maxTemp;

    @PositiveOrZero(message = "Expected yield cannot be negative")
    private Double expectedYieldPerUnit;

    @PositiveOrZero(message = "Market price cannot be negative")
    private Double currentMarketPrice;
}
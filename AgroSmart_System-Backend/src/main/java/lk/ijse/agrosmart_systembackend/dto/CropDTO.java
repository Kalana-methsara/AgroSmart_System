package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
}

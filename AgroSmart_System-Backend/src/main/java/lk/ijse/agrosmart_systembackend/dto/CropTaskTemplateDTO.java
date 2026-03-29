package lk.ijse.agrosmart_systembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CropTaskTemplateDTO implements Serializable {

    private String templateId;

    private String taskName;

    private Integer startDay;

    private Integer repeatInterval;

    private Double standardDuration;

    private String cropId;
}
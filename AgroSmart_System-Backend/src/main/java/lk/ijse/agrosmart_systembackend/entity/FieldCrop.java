package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FieldCrop implements Serializable {
    @Id
    private String fieldCropId;
    @ManyToOne
    @JoinColumn(name = "field_id", referencedColumnName = "fieldId")
    private Field field;
    @ManyToOne
    @JoinColumn(name = "crop_id", referencedColumnName = "cropId")
    private Crop crop;
    private LocalDate assignedDate;
}

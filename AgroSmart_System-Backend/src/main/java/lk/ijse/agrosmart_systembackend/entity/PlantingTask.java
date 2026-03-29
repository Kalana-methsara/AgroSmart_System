package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PlantingTask {
    @Id
    private String taskId;
    private String taskName;
    private LocalDate dueDate;
    private String status; // PENDING, COMPLETED
    private Double calculatedDuration; // ඔයාගේ අක්කර logic එකෙන් එන කාලය

    @ManyToOne
    @JoinColumn(name = "field_crop_id") // ඉඩම සහ බෝගය සම්බන්ධ වන record එක
    private FieldCrop fieldCrop;
}
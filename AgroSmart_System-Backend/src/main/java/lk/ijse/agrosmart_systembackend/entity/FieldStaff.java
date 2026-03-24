package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FieldStaff implements Serializable {
    @Id
    private String fieldStaffId;
    @ManyToOne
    @JoinColumn(name = "field_id")
    @ToString.Exclude
    private Field field;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;
}

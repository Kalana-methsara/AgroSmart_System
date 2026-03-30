package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Log {

    @Id
    private String logId;

    private LocalDate date;
    private String details;
    private String temperature;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FieldCrop> fieldCrops = new ArrayList<>();

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FieldStaff> fieldStaffs = new ArrayList<>();
}

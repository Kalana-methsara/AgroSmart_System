package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Log {
    @Id
    private String logId;
    private LocalDate date;
    private String details;
    private String temperature;
    @Column(columnDefinition = "LONGTEXT")
    private String observedImg;
    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;
    @ManyToOne
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;
    @OneToMany(mappedBy = "logEntity", cascade = CascadeType.ALL)
    private List<StaffLog> staffLog = new ArrayList<>();
}

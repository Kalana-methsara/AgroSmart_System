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
    @Column(columnDefinition = "LONGTEXT")
    private String observedImg;
    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;
    @ManyToOne
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;
    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL)
    private List<StaffLog> staffLog = new ArrayList<>();
    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL)
    private List<LogInventory> inventoryDetails = new ArrayList<>();
}

package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle implements Serializable {
    @Id
    private String vehicleId;
    private String category;
    @Column(unique = true)
    private String numberPlate;
    private String fuelType;
    private String status;
    private String remarks;
    @OneToMany(mappedBy = "vehicleId", cascade = CascadeType.ALL)
    private List<Staff> staff;
}

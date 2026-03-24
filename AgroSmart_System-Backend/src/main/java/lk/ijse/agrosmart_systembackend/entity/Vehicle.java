package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
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

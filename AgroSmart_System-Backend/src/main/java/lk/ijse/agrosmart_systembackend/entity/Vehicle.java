package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private String numberPlate;
    private String fuelType;
    private String status;
    private String remarks;
    @OneToMany(mappedBy = "vehicleId", cascade = CascadeType.ALL)
    private List<Staff> staff;
}

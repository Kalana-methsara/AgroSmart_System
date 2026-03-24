package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lk.ijse.agrosmart_systembackend.entity.enums.Gender;
import lk.ijse.agrosmart_systembackend.entity.enums.Role;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Staff implements Serializable {
    @Id
    private String staffId;
    private String fullName;
    private String designation;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String contactNumber;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<Equipment> equipment;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicleId;
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<FieldStaff> fieldStaff = new ArrayList<>();
    @OneToMany(mappedBy = "staffEntity", cascade = CascadeType.ALL)
    private List<StaffLog> staffLog = new ArrayList<>();
}

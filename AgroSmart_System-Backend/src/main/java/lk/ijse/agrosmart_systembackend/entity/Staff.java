package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Staff {

    @Id
    private String staffId;

    private String fullName;

    private String designation;

    private String position;

    private String bankName;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StaffStatus status = StaffStatus.ACTIVE;

    public enum StaffStatus {
        ACTIVE, INACTIVE
    }
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<FieldStaff> fieldStaff = new ArrayList<>();
}

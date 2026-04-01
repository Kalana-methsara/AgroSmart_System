package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lk.ijse.agrosmart_systembackend.entity.enums.Role;
import lk.ijse.agrosmart_systembackend.entity.enums.StaffStatus;
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

    @NotBlank(message = "Contact number cannot be blank")
    @Size(max = 15, message = "Contact number must be at most 15 characters")
    private String contactNumber;
    @Email(message = "Invalid email format")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String designation;

    private String position;

    private String bankName;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StaffStatus status = StaffStatus.ACTIVE;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<FieldStaff> fieldStaff = new ArrayList<>();
}

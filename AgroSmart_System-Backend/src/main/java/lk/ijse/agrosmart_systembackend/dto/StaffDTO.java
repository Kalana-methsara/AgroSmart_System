package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lk.ijse.agrosmart_systembackend.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO implements Serializable {

    private String staffId;

    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;

    private String contactNumber;

    private String email;

    private Role role;

    @NotBlank(message = "Designation cannot be blank")
    @Size(max = 50, message = "Designation must be at most 50 characters")
    private String designation;

    @NotBlank(message = "Job position cannot be blank")
    private String position;

    private String bankName;

    private String accountNumber;

    private String status;
}
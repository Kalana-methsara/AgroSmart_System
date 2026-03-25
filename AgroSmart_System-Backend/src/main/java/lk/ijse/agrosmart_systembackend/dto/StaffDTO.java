package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lk.ijse.agrosmart_systembackend.entity.enums.Gender;
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

    @NotBlank(message = "Designation cannot be blank")
    @Size(max = 50, message = "Designation must be at most 50 characters")
    private String designation;

    private Gender gender;

    @NotBlank(message = "Contact number cannot be blank")
    @Size(max = 15, message = "Contact number must be at most 15 characters")
    private String contactNumber;

    @Email(message = "Invalid email format")
    private String email;

    private Role role;

    private String vehicleId;
}
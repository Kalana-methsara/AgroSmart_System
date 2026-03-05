package lk.ijse.agrosmart_systembackend.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String email;
    private String password;
    private String role;
}
package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.AuthDTO;
import lk.ijse.agrosmart_systembackend.dto.AuthResponseDTO;
import lk.ijse.agrosmart_systembackend.dto.RegisterDTO;

public interface UserService {

    String saveUser(RegisterDTO registerDTO);
    AuthResponseDTO authenticate(AuthDTO authDTO);
    void generatePasswordResetCode(String email);
    String verifyResetCode(String email, String code);
    void resendPasswordResetCode(String email);
    void resetPassword(String email, String token, String newPassword);
}
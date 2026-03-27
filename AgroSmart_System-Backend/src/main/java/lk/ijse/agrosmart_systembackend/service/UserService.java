package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.*;

import java.util.List;

public interface UserService {

    String saveUser(RegisterDTO registerDTO);
    AuthResponseDTO authenticate(AuthDTO authDTO);
    void generatePasswordResetCode(String email);
    String verifyResetCode(String email, String code);
    void resendPasswordResetCode(String email);
    void resetPassword(String email, String token, String newPassword);
    List<UserDTO> getAllUsers();
    String deleteUser(Long id);
    String updateUser(UserDTO userDTO);
    UserDTO getUserDetails(String identifier);
}
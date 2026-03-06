package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lk.ijse.agrosmart_systembackend.dto.AuthDTO;
import lk.ijse.agrosmart_systembackend.dto.RegisterDTO;
import lk.ijse.agrosmart_systembackend.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final UserService authService;
    @PostMapping("register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(new ApiResponse
                (200,"OK",authService.saveUser(registerDTO)));
    }
    @PostMapping("login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody AuthDTO authDTO) {
        return ResponseEntity.ok(new ApiResponse(
                200,"OK",authService.authenticate(authDTO)
        ));
    }

}

package lk.ijse.agrosmart_systembackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lk.ijse.agrosmart_systembackend.entity.enums.Role;
import lk.ijse.agrosmart_systembackend.service.UserService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lk.ijse.agrosmart_systembackend.dto.AuthDTO;
import lk.ijse.agrosmart_systembackend.dto.RegisterDTO;
import lk.ijse.agrosmart_systembackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final UserService authService;

    @PostMapping("register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(new ApiResponse(200, "OK", authService.saveUser(registerDTO)));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody AuthDTO authDTO) {
        return ResponseEntity.ok(new ApiResponse(200, "OK", authService.authenticate(authDTO)));
    }
    @GetMapping("oauth/token")
    public ResponseEntity<ApiResponse> getOAuthToken(HttpServletRequest request) {
        try {
            // Get the authenticated user from SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();

                // Generate JWT token for the OAuth user
                JwtUtil jwtUtil = new JwtUtil();
                String token = jwtUtil.generateToken(email, Role.USER);

                Map<String, String> response = new HashMap<>();
                response.put("accessToken", token);
                response.put("tokenType", "Bearer");

                return ResponseEntity.ok(new ApiResponse(200, "OK", response));
            } else {
                return ResponseEntity.status(401).body(new ApiResponse(401, "Unauthorized", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(500, "Error", e.getMessage()));
        }
    }
}
package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.PasswordResetDTO;
import lk.ijse.agrosmart_systembackend.dto.VerifyCodeDTO;
import lk.ijse.agrosmart_systembackend.service.UserService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class PasswordResetController {

    private final UserService userService;

    @PostMapping("forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody PasswordResetDTO.ForgotPasswordRequest request) {
            userService.generatePasswordResetCode(request.getEmail());
            return ResponseEntity.ok(new ApiResponse(200, "Reset code sent to your email", null));
    }

    @PostMapping("verify-reset-code")
    public ResponseEntity<ApiResponse> verifyResetCode(@RequestBody VerifyCodeDTO request) {
            String token = userService.verifyResetCode(request.getEmail(), request.getCode());
            return ResponseEntity.ok(new ApiResponse(200, "Code verified successfully", token));
    }

    @PostMapping("resend-reset-code")
    public ResponseEntity<ApiResponse> resendResetCode(@RequestBody PasswordResetDTO.ForgotPasswordRequest request) {
            userService.resendPasswordResetCode(request.getEmail());
            return ResponseEntity.ok(new ApiResponse(200, "Reset code resent successfully", null));

    }

    @PostMapping("reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody PasswordResetDTO.ResetPasswordRequest request) {
            userService.resetPassword(request.getEmail(), request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse(200, "Password reset successful", null));
    }
}
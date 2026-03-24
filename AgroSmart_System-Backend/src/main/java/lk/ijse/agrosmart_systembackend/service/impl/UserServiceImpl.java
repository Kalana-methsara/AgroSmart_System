package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.AuthDTO;
import lk.ijse.agrosmart_systembackend.dto.AuthResponseDTO;
import lk.ijse.agrosmart_systembackend.dto.RegisterDTO;
import lk.ijse.agrosmart_systembackend.entity.PasswordResetToken;
import lk.ijse.agrosmart_systembackend.entity.Role;
import lk.ijse.agrosmart_systembackend.entity.User;
import lk.ijse.agrosmart_systembackend.repository.PasswordResetTokenRepository;
import lk.ijse.agrosmart_systembackend.repository.UserRepository;
import lk.ijse.agrosmart_systembackend.service.UserService;
import lk.ijse.agrosmart_systembackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;

    @Value("${app.reset-token-expiration}")
    private long resetTokenExpiration;

    @Override
    public String saveUser(RegisterDTO registerDTO){
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()){
            throw new RuntimeException("Username is already in use");
        }
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()){
            throw new IllegalStateException("Email is already in use");
        }
        User user = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(Role.valueOf(registerDTO.getRole()))
                .build();
        userRepository.save(user);
        return "User registered successfully";
    }

    public AuthResponseDTO authenticate(AuthDTO authDTO){
        User user;
        String loginInput = authDTO.getUsername(); // This can be username OR email

        // Check if the input contains @ (likely an email)
        if (loginInput != null && loginInput.contains("@")) {
            // Try to find by email first
            user = userRepository.findByEmail(loginInput)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginInput));
        } else {
            // Try to find by username
            user = userRepository.findByUsername(loginInput)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + loginInput));
        }

        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Wrong password");
        }

        // Use the actual username from the database for token generation
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponseDTO(token);
    }

    @Transactional
    public void generatePasswordResetCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found in our records"));

        // Delete any existing unused tokens for this email
        tokenRepository.findByEmailAndUsedFalse(email)
                .ifPresent(tokenRepository::delete);

        // Generate 6-digit code
        String code = String.format("%06d", new SecureRandom().nextInt(999999));

        // Generate temporary token for reset session
        String tempToken = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .email(email)
                .code(code)
                .token(tempToken)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15)) // 15 minutes expiry
                .used(false)
                .build();

        tokenRepository.save(resetToken);

        // Send email with code
        sendResetCodeEmail(email, code);
    }

    @Override
    @Transactional
    public String verifyResetCode(String email, String code) {
        PasswordResetToken resetToken = tokenRepository
                .findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification code"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Verification code has expired");
        }

        // Mark as used to prevent reuse
//        resetToken.setUsed(true);
//        tokenRepository.save(resetToken);

        // Return the session token for password reset
        return resetToken.getToken();
    }

    @Transactional
    public void resendPasswordResetCode(String email) {
        // Delete old tokens
        tokenRepository.findByEmailAndUsedFalse(email)
                .ifPresent(tokenRepository::delete);

        // Generate new code
        generatePasswordResetCode(email);
    }

    @Transactional
    public void resetPassword(String email, String token, String newPassword) {
        // Find token by email, token, and not used
        PasswordResetToken resetToken = tokenRepository
                .findByEmailAndTokenAndUsedFalse(email, token)
                .orElseThrow(() -> new RuntimeException("Invalid reset session"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Reset session has expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete the used token (or mark as used)
        tokenRepository.delete(resetToken);

        // Send confirmation email
        sendPasswordResetConfirmationEmail(email);
    }


    private void sendResetCodeEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("AgroSmart - Password Reset Code");
        message.setText(String.format(
                "Hello,\n\n" +
                        "You have requested to reset your password. Use the following code to verify your identity:\n\n" +
                        "🔐 Reset Code: %s\n\n" +
                        "This code will expire in 15 minutes.\n\n" +
                        "If you didn't request this, please ignore this email or contact support.\n\n" +
                        "Best regards,\n" +
                        "AgroSmart Team", code));

        mailSender.send(message);
    }

    private void sendPasswordResetConfirmationEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("AgroSmart - Password Reset Successful");
        message.setText(String.format(
                "Hello,\n\n" +
                        "Your AgroSmart account password has been successfully reset.\n\n" +
                        "If you didn't perform this action, please contact our support team immediately.\n\n" +
                        "Best regards,\n" +
                        "AgroSmart Team"));

        mailSender.send(message);
    }
}
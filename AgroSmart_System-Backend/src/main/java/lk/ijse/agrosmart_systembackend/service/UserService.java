
package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.AuthDTO;
import lk.ijse.agrosmart_systembackend.dto.AuthResponseDTO;
import lk.ijse.agrosmart_systembackend.dto.RegisterDTO;
import lk.ijse.agrosmart_systembackend.entity.Role;
import lk.ijse.agrosmart_systembackend.entity.User;
import lk.ijse.agrosmart_systembackend.repository.UserRepository;
import lk.ijse.agrosmart_systembackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String saveUser(RegisterDTO registerDTO){
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()){
            throw new RuntimeException("Username is already in use");
        }
        User user=User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(Role.valueOf(registerDTO.getRole()))
                .build();
        userRepository.save(user);
        return "User registered successfully";
    }
    public AuthResponseDTO authenticate(AuthDTO authDTO){
        User user=userRepository.findByUsername(authDTO.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("Username not found"));
        if (!passwordEncoder.matches(authDTO.getPassword(),user.getPassword())){
            throw new BadCredentialsException("Wrong password");
        }
        String token=jwtUtil.generateToken(authDTO.getUsername(),user.getRole());
        return new AuthResponseDTO(token);
    }
}

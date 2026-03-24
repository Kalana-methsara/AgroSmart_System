package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lk.ijse.agrosmart_systembackend.entity.enums.Role;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
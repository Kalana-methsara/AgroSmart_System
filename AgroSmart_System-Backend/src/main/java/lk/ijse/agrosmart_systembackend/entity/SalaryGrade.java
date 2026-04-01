package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lk.ijse.agrosmart_systembackend.entity.enums.Role;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SalaryGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String grade;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal minSalary;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal maxSalary;
}

package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false, length = 100)
    private String position;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal minSalary;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal maxSalary;
}

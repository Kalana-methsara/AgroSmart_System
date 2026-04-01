package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalaryGradeDTO implements Serializable {
        private Long id;
        @NotBlank
        @Size(max = 10)
        private String grade;
        @NotBlank
        private String position;
        @NotNull
        @DecimalMin("0.00")
        private BigDecimal minSalary;
        @NotNull @DecimalMin("0.00")
        private BigDecimal maxSalary;
}

package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.*;
import lk.ijse.agrosmart_systembackend.entity.enums.SettingType;
import lk.ijse.agrosmart_systembackend.entity.enums.ValueType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalarySettingDTO implements Serializable {

        private Long id;

        @NotBlank(message = "Setting name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        private String name;

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        private String description;

        @NotNull(message = "Setting type (ALLOWANCE, DEDUCTION, TAX) is required")
        private SettingType type;

        @NotNull(message = "Value is required")
        @DecimalMin("0.00")
        private BigDecimal value;

        @NotNull(message = "Value type (FIXED, PERCENTAGE) is required")
        private ValueType valueType;

        private Boolean isActive = true;


}
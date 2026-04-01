package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.*;
import lk.ijse.agrosmart_systembackend.entity.enums.PaymentMethod;
import lk.ijse.agrosmart_systembackend.entity.enums.SalaryStatus;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalaryRecordDTO implements Serializable {
    private Long id;

    @NotBlank(message = "Staff ID is required")
    private String staffId;

    @NotNull(message = "Basic salary is required")
    @PositiveOrZero(message = "Basic salary cannot be negative")
    private BigDecimal basicSalary;

    @PositiveOrZero(message = "Allowances cannot be negative")
    private BigDecimal allowances = BigDecimal.ZERO;

    @PositiveOrZero(message = "Deductions cannot be negative")
    private BigDecimal deductions = BigDecimal.ZERO;

    private LocalDate paymentDate;

    private PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;

    private SalaryStatus status = SalaryStatus.PENDING;

    @NotNull
    @Min(1)
    @Max(12)
    private Integer payrollMonth;

    @NotNull
    @Min(2000)
    private Integer payrollYear;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;


}
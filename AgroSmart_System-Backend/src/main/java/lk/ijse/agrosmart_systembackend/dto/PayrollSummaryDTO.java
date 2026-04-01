package lk.ijse.agrosmart_systembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data  @NoArgsConstructor @AllArgsConstructor @Builder
public class PayrollSummaryDTO {
    private int month;
    private int year;
    private long totalEmployees;
    private BigDecimal totalBasicSalary;
    private BigDecimal totalDeductions;
    private BigDecimal netPayroll;
    private long paidCount;
    private long pendingCount;
}

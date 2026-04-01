package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lk.ijse.agrosmart_systembackend.entity.enums.PaymentMethod;
import lk.ijse.agrosmart_systembackend.entity.enums.SalaryStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SalaryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn( nullable = false)
    private Staff staff;

    @Column( nullable = false, precision = 12, scale = 2)
    private BigDecimal basicSalary;

    @Column( precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal allowances = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal deductions = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal netSalary;

    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SalaryStatus status = SalaryStatus.PENDING;

    @Column(nullable = false)
    private Integer payrollMonth;  // 1–12

    @Column( nullable = false)
    private Integer payrollYear;

    @Column(length = 500)
    private String notes;

    @Column( updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void computeNetSalary() {
        if (basicSalary != null) {
            BigDecimal allowancesVal = allowances != null ? allowances : BigDecimal.ZERO;
            BigDecimal deductionsVal = deductions != null ? deductions : BigDecimal.ZERO;
            this.netSalary = basicSalary.add(allowancesVal).subtract(deductionsVal);
        }
    }


}

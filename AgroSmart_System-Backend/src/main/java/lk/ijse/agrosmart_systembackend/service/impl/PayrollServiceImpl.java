package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.PayrollSummaryDTO;
import lk.ijse.agrosmart_systembackend.entity.SalaryRecord;
import lk.ijse.agrosmart_systembackend.entity.SalarySetting;
import lk.ijse.agrosmart_systembackend.entity.Staff;
import lk.ijse.agrosmart_systembackend.entity.enums.*;
import lk.ijse.agrosmart_systembackend.repository.SalaryRecordRepository;
import lk.ijse.agrosmart_systembackend.repository.SalarySettingRepository;
import lk.ijse.agrosmart_systembackend.repository.StaffRepository;
import lk.ijse.agrosmart_systembackend.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollServiceImpl implements PayrollService {

    private final SalaryRecordRepository salaryRecordRepo;
    private final StaffRepository staffRepository;
    private final SalarySettingRepository salarySettingRepo;
    private final ModelMapper modelMapper;

    @Override
    public PayrollSummaryDTO getSummary(int month, int year) {
        List<SalaryRecord> records = salaryRecordRepo.findByPayrollMonthAndPayrollYear(month, year);

        BigDecimal totalBasic = records.stream()
                .map(SalaryRecord::getBasicSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDeductions = records.stream()
                .map(SalaryRecord::getDeductions)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netPayroll = records.stream()
                .map(SalaryRecord::getNetSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long paidCount = records.stream().filter(r -> r.getStatus() == SalaryStatus.PAID).count();
        long pendingCount = records.stream().filter(r -> r.getStatus() == SalaryStatus.PENDING).count();

        return PayrollSummaryDTO.builder()
                .month(month)
                .year(year)
                .totalEmployees(records.size())
                .totalBasicSalary(totalBasic)
                .totalDeductions(totalDeductions)
                .netPayroll(netPayroll)
                .paidCount(paidCount)
                .pendingCount(pendingCount)
                .build();
    }

    @Override
    public String calculatePayroll(int month, int year) {
        List<SalarySetting> activeSettings = salarySettingRepo.findAll().stream()
                .filter(SalarySetting::getIsActive)
                .toList();

        List<Staff> activeEmployees = staffRepository.findByStatus(StaffStatus.ACTIVE);
        int createdCount = 0;

        for (Staff emp : activeEmployees) {
            boolean alreadyExists = salaryRecordRepo
                    .findByStaff_StaffIdAndPayrollMonthAndPayrollYear(emp.getStaffId(), month, year)
                    .isPresent();

            if (alreadyExists) continue;

            // Note: Basic salary logic usually comes from a 'SalaryGrade' or 'Staff' field
            // Defaulting to 0 as per your previous logic
            BigDecimal basic = BigDecimal.ZERO;

            BigDecimal totalAllowances = computeAllowances(activeSettings, basic);
            BigDecimal totalDeductions = computeDeductions(activeSettings, basic);
            BigDecimal net = basic.add(totalAllowances).subtract(totalDeductions);

            SalaryRecord record = SalaryRecord.builder()
                    .staff(emp)
                    .basicSalary(basic)
                    .allowances(totalAllowances)
                    .deductions(totalDeductions)
                    .netSalary(net)
                    .paymentMethod(PaymentMethod.BANK_TRANSFER)
                    .status(SalaryStatus.PENDING)
                    .payrollMonth(month)
                    .payrollYear(year)
                    .notes("Auto-calculated")
                    .build();

            salaryRecordRepo.save(record);
            createdCount++;
        }

        return "Payroll calculated successfully for " + createdCount + " employees";
    }

    @Override
    public String processPayments(int month, int year) {
        List<SalaryRecord> records = salaryRecordRepo.findByPayrollMonthAndPayrollYear(month, year);
        int processedCount = 0;

        for (SalaryRecord record : records) {
            if (record.getStatus() == SalaryStatus.PENDING) {
                record.setStatus(SalaryStatus.PAID);
                record.setPaymentDate(LocalDate.now());
                salaryRecordRepo.save(record);
                processedCount++;
            }
        }
        return "Payments processed successfully for " + processedCount + " records";
    }

    // ── Mapping Helpers (ModelMapper used for DTO lists if needed elsewhere) ────────

    private BigDecimal computeAllowances(List<SalarySetting> settings, BigDecimal basic) {
        return settings.stream()
                .filter(s -> s.getType() == SettingType.ALLOWANCE)
                .map(s -> computeValue(s, basic))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal computeDeductions(List<SalarySetting> settings, BigDecimal basic) {
        return settings.stream()
                .filter(s -> s.getType() == SettingType.DEDUCTION || s.getType() == SettingType.TAX)
                .map(s -> computeValue(s, basic))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal computeValue(SalarySetting setting, BigDecimal basic) {
        if (setting.getValueType() == ValueType.PERCENTAGE) {
            return basic.multiply(setting.getValue())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        return setting.getValue();
    }
}
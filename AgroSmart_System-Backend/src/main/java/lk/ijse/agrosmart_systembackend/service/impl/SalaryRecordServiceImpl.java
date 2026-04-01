package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.SalaryRecordDTO;
import lk.ijse.agrosmart_systembackend.entity.SalaryRecord;
import lk.ijse.agrosmart_systembackend.entity.Staff;
import lk.ijse.agrosmart_systembackend.entity.enums.SalaryStatus;
import lk.ijse.agrosmart_systembackend.repository.SalaryRecordRepository;
import lk.ijse.agrosmart_systembackend.repository.StaffRepository;
import lk.ijse.agrosmart_systembackend.service.SalaryRecordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalaryRecordServiceImpl implements SalaryRecordService {

    private final SalaryRecordRepository salaryRecordRepo;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SalaryRecordDTO> findAll() {
        return modelMapper.map(salaryRecordRepo.findAll(), new TypeToken<List<SalaryRecordDTO>>() {}.getType());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryRecordDTO> search(String search, String status, String department) {
        SalaryStatus statusEnum = (status != null && !status.isBlank()) ? SalaryStatus.valueOf(status.toUpperCase()) : null;
        String deptParam = (department != null && !department.isBlank()) ? department : null;
        String searchParam = (search != null && !search.isBlank()) ? search : null;

        List<SalaryRecord> records = salaryRecordRepo.searchRecords(searchParam, statusEnum, deptParam);
        return modelMapper.map(records, new TypeToken<List<SalaryRecordDTO>>() {}.getType());
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryRecordDTO findById(Long id) {
        SalaryRecord record = getOrThrow(id);
        return modelMapper.map(record, SalaryRecordDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryRecordDTO> findByEmployee(String employeeId) {
        List<SalaryRecord> records = salaryRecordRepo.findByStaff_StaffId(employeeId);
        return modelMapper.map(records, new TypeToken<List<SalaryRecordDTO>>() {}.getType());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryRecordDTO> findByPeriod(int month, int year) {
        List<SalaryRecord> records = salaryRecordRepo.findByPayrollMonthAndPayrollYear(month, year);
        return modelMapper.map(records, new TypeToken<List<SalaryRecordDTO>>() {}.getType());
    }

    @Override
    public String create(SalaryRecordDTO dto) {
        Staff staff = staffRepository.findById(dto.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + dto.getStaffId()));

        // Check for existing record to prevent duplicates
        salaryRecordRepo.findByStaff_StaffIdAndPayrollMonthAndPayrollYear(
                        dto.getStaffId(), dto.getPayrollMonth(), dto.getPayrollYear())
                .ifPresent(s -> { throw new RuntimeException("Salary record already exists for this period"); });

        SalaryRecord record = buildEntity(dto, staff);
        salaryRecordRepo.save(record);
        return "Salary Record saved successfully";
    }

    @Override
    public String update(SalaryRecordDTO dto) {
        SalaryRecord existing = getOrThrow(dto.getId());
        Staff staff = staffRepository.findById(dto.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + dto.getStaffId()));

        BigDecimal allowances = dto.getAllowances() != null ? dto.getAllowances() : BigDecimal.ZERO;
        BigDecimal deductions = dto.getDeductions() != null ? dto.getDeductions() : BigDecimal.ZERO;

        existing.setStaff(staff);
        existing.setBasicSalary(dto.getBasicSalary());
        existing.setAllowances(allowances);
        existing.setDeductions(deductions);
        existing.setNetSalary(dto.getBasicSalary().add(allowances).subtract(deductions));
        existing.setPaymentDate(dto.getPaymentDate());
        existing.setPaymentMethod(dto.getPaymentMethod());
        existing.setStatus(dto.getStatus());
        existing.setPayrollMonth(dto.getPayrollMonth());
        existing.setPayrollYear(dto.getPayrollYear());
        existing.setNotes(dto.getNotes());

        salaryRecordRepo.save(existing);
        return "Salary Record updated successfully";
    }

    @Override
    public String updateStatus(Long id, String status) {
        SalaryRecord record = getOrThrow(id);
        record.setStatus(SalaryStatus.valueOf(status.toUpperCase()));
        salaryRecordRepo.save(record);
        return "Salary Record status updated to " + status;
    }

    @Override
    public String delete(Long id) {
        if (!salaryRecordRepo.existsById(id)) {
            throw new RuntimeException("Salary Record not found with ID: " + id);
        }
        salaryRecordRepo.deleteById(id);
        return "Salary Record deleted successfully";
    }

    private SalaryRecord getOrThrow(Long id) {
        return salaryRecordRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary Record not found with ID: " + id));
    }

    private SalaryRecord buildEntity(SalaryRecordDTO dto, Staff staff) {
        BigDecimal allowances = dto.getAllowances() != null ? dto.getAllowances() : BigDecimal.ZERO;
        BigDecimal deductions = dto.getDeductions() != null ? dto.getDeductions() : BigDecimal.ZERO;
        BigDecimal netSalary = dto.getBasicSalary().add(allowances).subtract(deductions);

        return SalaryRecord.builder()
                .staff(staff)
                .basicSalary(dto.getBasicSalary())
                .allowances(allowances)
                .deductions(deductions)
                .netSalary(netSalary)
                .paymentDate(dto.getPaymentDate())
                .paymentMethod(dto.getPaymentMethod())
                .status(dto.getStatus() != null ? dto.getStatus() : SalaryStatus.PENDING)
                .payrollMonth(dto.getPayrollMonth())
                .payrollYear(dto.getPayrollYear())
                .notes(dto.getNotes())
                .build();
    }
}
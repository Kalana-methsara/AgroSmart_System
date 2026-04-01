package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.SalaryRecordDTO;
import lk.ijse.agrosmart_systembackend.entity.enums.SalaryStatus;

import java.util.List;

public interface SalaryRecordService {
    List<SalaryRecordDTO> findAll();
    List<SalaryRecordDTO> search(String search, String status, String department);
    SalaryRecordDTO findById(Long id);
    List<SalaryRecordDTO> findByEmployee(String employeeId);
    List<SalaryRecordDTO> findByPeriod(int month, int year);
    String create(SalaryRecordDTO dto);
    String update(SalaryRecordDTO dto);
    String updateStatus(Long id, String status);
    String delete(Long id);
}
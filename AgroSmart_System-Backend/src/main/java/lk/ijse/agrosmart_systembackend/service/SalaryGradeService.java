package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.SalaryGradeDTO;
import java.util.List;

public interface SalaryGradeService {
    List<SalaryGradeDTO> findAll();
    SalaryGradeDTO findById(Long id);
    String create(SalaryGradeDTO dto);
    String update(SalaryGradeDTO dto);
    String delete(Long id);
}
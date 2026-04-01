package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.SalarySettingDTO;
import lk.ijse.agrosmart_systembackend.entity.enums.SettingType;

import java.util.List;

public interface SalarySettingService {

    List<SalarySettingDTO> findAll();
    List<SalarySettingDTO> findByType(SettingType type);
    SalarySettingDTO findById(Long id);
    String create(SalarySettingDTO dto);
    String update(SalarySettingDTO dto);
    String delete(Long id);
}
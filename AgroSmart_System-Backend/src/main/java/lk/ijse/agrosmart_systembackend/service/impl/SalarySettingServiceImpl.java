package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.SalarySettingDTO;
import lk.ijse.agrosmart_systembackend.entity.SalarySetting;
import lk.ijse.agrosmart_systembackend.entity.enums.SettingType;
import lk.ijse.agrosmart_systembackend.repository.SalarySettingRepository;
import lk.ijse.agrosmart_systembackend.service.SalarySettingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalarySettingServiceImpl  implements SalarySettingService {

    private final SalarySettingRepository salarySettingRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<SalarySettingDTO> findAll() {
        List<SalarySetting> settings = salarySettingRepo.findAll();
        return modelMapper.map(settings, new TypeToken<List<SalarySettingDTO>>() {}.getType());
    }

    @Override
    public List<SalarySettingDTO> findByType(SettingType type) {
        List<SalarySetting> settings = salarySettingRepo.findByType(type);
        return modelMapper.map(settings, new TypeToken<List<SalarySettingDTO>>() {}.getType());
    }

    @Override
    @Transactional(readOnly = true)
    public SalarySettingDTO findById(Long id) {
        SalarySetting setting = salarySettingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary Setting with ID " + id + " not found"));
        return modelMapper.map(setting, SalarySettingDTO.class);
    }

    @Override
    public String create(SalarySettingDTO dto) {
        SalarySetting setting = SalarySetting.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .type(dto.getType())
                .value(dto.getValue())
                .valueType(dto.getValueType())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();

        salarySettingRepo.save(setting);
        return "Salary Setting saved successfully";
    }

    @Override
    public String update(SalarySettingDTO dto) {
        SalarySetting existingSetting = salarySettingRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Salary Setting with ID " + dto.getId() + " not found"));

        existingSetting.setName(dto.getName());
        existingSetting.setDescription(dto.getDescription());
        existingSetting.setType(dto.getType());
        existingSetting.setValue(dto.getValue());
        existingSetting.setValueType(dto.getValueType());
        if (dto.getIsActive() != null) {
            existingSetting.setIsActive(dto.getIsActive());
        }

        salarySettingRepo.save(existingSetting);
        return "Salary Setting updated successfully";
    }

    @Override
    public String delete(Long id) {
        SalarySetting setting = salarySettingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary Setting with ID " + id + " not found"));

        salarySettingRepo.delete(setting);
        return "Salary Setting deleted successfully";
    }
}
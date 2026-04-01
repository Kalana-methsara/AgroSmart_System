package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.SalaryGradeDTO;
import lk.ijse.agrosmart_systembackend.entity.SalaryGrade;
import lk.ijse.agrosmart_systembackend.repository.SalaryGradeRepository;
import lk.ijse.agrosmart_systembackend.service.SalaryGradeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalaryGradeServiceImpl implements SalaryGradeService {

    private final SalaryGradeRepository salaryGradeRepo;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SalaryGradeDTO> findAll() {
        List<SalaryGrade> grades = salaryGradeRepo.findAll();
        return modelMapper.map(grades, new TypeToken<List<SalaryGradeDTO>>() {}.getType());
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryGradeDTO findById(Long id) {
        SalaryGrade grade = salaryGradeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary Grade not found with ID: " + id));
        return modelMapper.map(grade, SalaryGradeDTO.class);
    }

    @Override
    public String create(SalaryGradeDTO dto) {
        if (salaryGradeRepo.existsByGrade(dto.getGrade())) {
            throw new RuntimeException("Salary grade " + dto.getGrade() + " already exists");
        }

        // Using Builder pattern for creation
        SalaryGrade grade = SalaryGrade.builder()
                .grade(dto.getGrade())
                .role(dto.getRole())
                .minSalary(dto.getMinSalary())
                .maxSalary(dto.getMaxSalary())
                .build();

        salaryGradeRepo.save(grade);
        return "Salary Grade saved successfully";
    }

    @Override
    public String update(SalaryGradeDTO dto) {
        SalaryGrade existingGrade = salaryGradeRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Salary Grade not found with ID: " + dto.getId()));

        // Check uniqueness if the grade name is being changed
        if (!existingGrade.getGrade().equals(dto.getGrade()) && salaryGradeRepo.existsByGrade(dto.getGrade())) {
            throw new RuntimeException("Salary grade " + dto.getGrade() + " already exists");
        }

        // Updating fields manually
        existingGrade.setGrade(dto.getGrade());
        existingGrade.setRole(dto.getRole());
        existingGrade.setMinSalary(dto.getMinSalary());
        existingGrade.setMaxSalary(dto.getMaxSalary());

        salaryGradeRepo.save(existingGrade);
        return "Salary Grade updated successfully";
    }

    @Override
    public String delete(Long id) {
        SalaryGrade grade = salaryGradeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary Grade not found with ID: " + id));

        salaryGradeRepo.delete(grade);
        return "Salary Grade deleted successfully";
    }
}
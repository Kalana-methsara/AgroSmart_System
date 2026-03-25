package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.FieldStaffDTO;
import lk.ijse.agrosmart_systembackend.entity.Field;
import lk.ijse.agrosmart_systembackend.entity.FieldStaff;
import lk.ijse.agrosmart_systembackend.entity.Staff;
import lk.ijse.agrosmart_systembackend.repository.FieldRepository;
import lk.ijse.agrosmart_systembackend.repository.FieldStaffRepository;
import lk.ijse.agrosmart_systembackend.repository.StaffRepository;
import lk.ijse.agrosmart_systembackend.service.FieldStaffService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FieldStaffServiceImpl implements FieldStaffService {

    private final FieldStaffRepository fieldStaffRepository;
    private final FieldRepository fieldRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Override
    public String assignStaffToField(FieldStaffDTO fieldStaffDTO) {
        // Check if assignment already exists
        if (fieldStaffRepository.findByField_FieldIdAndStaff_StaffId(
                fieldStaffDTO.getFieldId(), fieldStaffDTO.getStaffId()).isPresent()) {
            throw new RuntimeException("Staff already assigned to this field");
        }

        // Fetch Field and Staff
        Field field = fieldRepository.findById(fieldStaffDTO.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found with ID: " + fieldStaffDTO.getFieldId()));

        Staff staff = staffRepository.findById(fieldStaffDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + fieldStaffDTO.getStaffId()));

        // Generate ID
        String newId = generateFieldStaffID();

        // Create assignment
        FieldStaff fieldStaff = FieldStaff.builder()
                .fieldStaffId(newId)
                .field(field)
                .staff(staff)
                .assignedDate(fieldStaffDTO.getAssignedDate() != null ?
                        fieldStaffDTO.getAssignedDate() : LocalDate.now())
                .build();

        fieldStaffRepository.save(fieldStaff);
        return "Staff assigned to field successfully";
    }

    @Override
    public String updateAssignment(FieldStaffDTO fieldStaffDTO) {
        FieldStaff existingAssignment = fieldStaffRepository
                .findByField_FieldIdAndStaff_StaffId(fieldStaffDTO.getFieldId(), fieldStaffDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Update assigned date
        if (fieldStaffDTO.getAssignedDate() != null) {
            existingAssignment.setAssignedDate(fieldStaffDTO.getAssignedDate());
            fieldStaffRepository.save(existingAssignment);
        }

        return "Assignment updated successfully";
    }

    @Override
    public String removeStaffFromField(String fieldId, String staffId) {
        FieldStaff assignment = fieldStaffRepository
                .findByField_FieldIdAndStaff_StaffId(fieldId, staffId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        fieldStaffRepository.delete(assignment);
        return "Staff removed from field successfully";
    }

    @Override
    public List<FieldStaffDTO> getStaffByField(String fieldId) {
        List<FieldStaff> assignments = fieldStaffRepository.findByField_FieldId(fieldId);
        return convertToDTOList(assignments);
    }

    @Override
    public List<FieldStaffDTO> getFieldsByStaff(String staffId) {
        List<FieldStaff> assignments = fieldStaffRepository.findByStaff_StaffId(staffId);
        return convertToDTOList(assignments);
    }

    @Override
    public List<FieldStaffDTO> getAllAssignments() {
        List<FieldStaff> assignments = fieldStaffRepository.findAll();
        return convertToDTOList(assignments);
    }

    private List<FieldStaffDTO> convertToDTOList(List<FieldStaff> assignments) {
        List<FieldStaffDTO> dtoList = new ArrayList<>();

        for (FieldStaff assignment : assignments) {
            FieldStaffDTO dto = new FieldStaffDTO();
            dto.setFieldStaffId(assignment.getFieldStaffId());
            dto.setFieldId(assignment.getField() != null ? assignment.getField().getFieldId() : null);
            dto.setStaffId(assignment.getStaff() != null ? assignment.getStaff().getStaffId() : null);
            dto.setAssignedDate(assignment.getAssignedDate());
            dtoList.add(dto);
        }

        return dtoList;
    }

    private String generateFieldStaffID() {
        List<FieldStaff> assignments = fieldStaffRepository.findAll();

        if (assignments.isEmpty()) {
            return "FS001";
        }

        String lastId = assignments.get(assignments.size() - 1).getFieldStaffId();
        int newId = Integer.parseInt(lastId.substring(2)) + 1;

        return String.format("FS%03d", newId);
    }
}
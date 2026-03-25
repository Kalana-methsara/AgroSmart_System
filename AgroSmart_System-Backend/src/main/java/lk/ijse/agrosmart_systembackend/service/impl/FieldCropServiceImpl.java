package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.FieldCropDTO;
import lk.ijse.agrosmart_systembackend.entity.Crop;
import lk.ijse.agrosmart_systembackend.entity.Field;
import lk.ijse.agrosmart_systembackend.entity.FieldCrop;
import lk.ijse.agrosmart_systembackend.repository.CropRepository;
import lk.ijse.agrosmart_systembackend.repository.FieldCropRepository;
import lk.ijse.agrosmart_systembackend.repository.FieldRepository;
import lk.ijse.agrosmart_systembackend.service.FieldCropService;
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
public class FieldCropServiceImpl implements FieldCropService {

    private final FieldCropRepository fieldCropRepository;
    private final FieldRepository fieldRepository;
    private final CropRepository cropRepository;
    private final ModelMapper modelMapper;

    @Override
    public String assignCropToField(FieldCropDTO fieldCropDTO) {
        // Check if assignment already exists
        if (fieldCropRepository.findByField_FieldIdAndCrop_CropId(
                fieldCropDTO.getFieldId(), fieldCropDTO.getCropId()).isPresent()) {
            throw new RuntimeException("Crop already assigned to this field");
        }

        // Fetch Field and Crop
        Field field = fieldRepository.findById(fieldCropDTO.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found with ID: " + fieldCropDTO.getFieldId()));

        Crop crop = cropRepository.findById(fieldCropDTO.getCropId())
                .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + fieldCropDTO.getCropId()));

        // Generate ID
        String newId = generateFieldCropID();

        // Create assignment
        FieldCrop fieldCrop = FieldCrop.builder()
                .fieldCropId(newId)
                .field(field)
                .crop(crop)
                .assignedDate(fieldCropDTO.getAssignedDate() != null ?
                        fieldCropDTO.getAssignedDate() : LocalDate.now())
                .build();

        fieldCropRepository.save(fieldCrop);
        return "Crop assigned to field successfully";
    }

    @Override
    public String updateAssignment(FieldCropDTO fieldCropDTO) {
        FieldCrop existingAssignment = fieldCropRepository
                .findByField_FieldIdAndCrop_CropId(fieldCropDTO.getFieldId(), fieldCropDTO.getCropId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Update assigned date
        if (fieldCropDTO.getAssignedDate() != null) {
            existingAssignment.setAssignedDate(fieldCropDTO.getAssignedDate());
            fieldCropRepository.save(existingAssignment);
        }

        return "Assignment updated successfully";
    }

    @Override
    public String removeCropFromField(String fieldId, String cropId) {
        FieldCrop assignment = fieldCropRepository
                .findByField_FieldIdAndCrop_CropId(fieldId, cropId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        fieldCropRepository.delete(assignment);
        return "Crop removed from field successfully";
    }

    @Override
    public List<FieldCropDTO> getCropsByField(String fieldId) {
        List<FieldCrop> assignments = fieldCropRepository.findByField_FieldId(fieldId);
        return convertToDTOList(assignments);
    }

    @Override
    public List<FieldCropDTO> getFieldsByCrop(String cropId) {
        List<FieldCrop> assignments = fieldCropRepository.findByCrop_CropId(cropId);
        return convertToDTOList(assignments);
    }

    @Override
    public List<FieldCropDTO> getAllAssignments() {
        List<FieldCrop> assignments = fieldCropRepository.findAll();
        return convertToDTOList(assignments);
    }

    private List<FieldCropDTO> convertToDTOList(List<FieldCrop> assignments) {
        List<FieldCropDTO> dtoList = new ArrayList<>();

        for (FieldCrop assignment : assignments) {
            FieldCropDTO dto = new FieldCropDTO();
            dto.setFieldCropId(assignment.getFieldCropId());
            dto.setFieldId(assignment.getField() != null ? assignment.getField().getFieldId() : null);
            dto.setCropId(assignment.getCrop() != null ? assignment.getCrop().getCropId() : null);
            dto.setAssignedDate(assignment.getAssignedDate());
            dtoList.add(dto);
        }

        return dtoList;
    }

    private String generateFieldCropID() {
        List<FieldCrop> assignments = fieldCropRepository.findAll();

        if (assignments.isEmpty()) {
            return "FC001";
        }

        String lastId = assignments.get(assignments.size() - 1).getFieldCropId();
        int newId = Integer.parseInt(lastId.substring(2)) + 1;

        return String.format("FC%03d", newId);
    }
}
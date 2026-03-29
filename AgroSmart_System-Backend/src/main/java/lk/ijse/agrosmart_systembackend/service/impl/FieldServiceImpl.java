package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.FieldDTO;
import lk.ijse.agrosmart_systembackend.entity.Field;
import lk.ijse.agrosmart_systembackend.repository.FieldRepository;
import lk.ijse.agrosmart_systembackend.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // Added for better data integrity
public class FieldServiceImpl implements FieldService {

    private final FieldRepository fieldRepository;
    private final ModelMapper modelMapper;

    @Override
    public String saveField(FieldDTO fieldDTO) {
        if (fieldRepository.existsByFieldName(fieldDTO.getFieldName())) {
            throw new RuntimeException("Field name '" + fieldDTO.getFieldName() + "' is already in use");
        }

        Field field = Field.builder()
                .fieldId(generateFieldID())
                .fieldName(fieldDTO.getFieldName())
                .locationName(fieldDTO.getLocationName())
                .coordinates(fieldDTO.getCoordinates())
                .area(fieldDTO.getArea())
                .areaUnit("acres")
                .description(fieldDTO.getDescription())
                .image(fieldDTO.getImage())
                .build();

        fieldRepository.save(field);
        return "Field saved successfully";
    }
    @Override
    @Transactional
    public String updateField(FieldDTO fieldDTO) {
        Field existingField = fieldRepository.findById(fieldDTO.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field with ID " + fieldDTO.getFieldId() + " not found"));

        if (fieldDTO.getFieldName() != null && !fieldDTO.getFieldName().equals(existingField.getFieldName())) {
            if (fieldRepository.existsByFieldName(fieldDTO.getFieldName())) {
                throw new RuntimeException("Field name '" + fieldDTO.getFieldName() + "' is already in use");
            }
            existingField.setFieldName(fieldDTO.getFieldName());
        }

        existingField.setLocationName(fieldDTO.getLocationName());
        existingField.setCoordinates(fieldDTO.getCoordinates());
        existingField.setArea(fieldDTO.getArea());
        existingField.setAreaUnit("acres");
        existingField.setDescription(fieldDTO.getDescription());

        if (fieldDTO.getImage() != null && !fieldDTO.getImage().isEmpty()) {
            existingField.setImage(fieldDTO.getImage());
        }

        fieldRepository.save(existingField);
        return "Field updated successfully";
    }

    @Override
    public String deleteField(String fieldId) {
        if (!fieldRepository.existsById(fieldId)) {
            throw new RuntimeException("Field with ID " + fieldId + " does not exist");
        }
        fieldRepository.deleteById(fieldId);
        return "Field deleted successfully";
    }

    @Override
    public List<FieldDTO> getAllFields() {
        List<Field> allFields = fieldRepository.findAll();
        return modelMapper.map(allFields, new TypeToken<List<FieldDTO>>() {}.getType());
    }

    private String generateFieldID() {
        // Optimization: Get only the last record by ID
        return fieldRepository.findAll().stream()
                .map(Field::getFieldId)
                .sorted((id1, id2) -> id2.compareTo(id1)) // Sort descending
                .findFirst()
                .map(lastId -> {
                    int nextNum = Integer.parseInt(lastId.substring(1)) + 1;
                    return String.format("F%03d", nextNum); // Formats to F001, F002, etc.
                })
                .orElse("F001");
    }
}
package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.FieldDTO;
import lk.ijse.agrosmart_systembackend.entity.Field;
import lk.ijse.agrosmart_systembackend.repository.FieldRepository;
import lk.ijse.agrosmart_systembackend.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;

    private final ModelMapper modelMapper;

    @Override
    public String saveField(FieldDTO fieldDTO){
        if (fieldRepository.existsById(fieldDTO.getFieldId())) {
            throw new RuntimeException("Field with ID " + fieldDTO.getFieldId() + " already exists");
        }
        if (fieldRepository.existsByFieldName(fieldDTO.getFieldName())) {
            throw new RuntimeException("Field name '" + fieldDTO.getFieldName() + "' is already in use");
        }
        Field fields = Field.builder()
                .fieldId(generateFieldID())
                .fieldName(fieldDTO.getFieldName())
                .size(fieldDTO.getSize())
                .location(fieldDTO.getLocation())
                .fieldImg1(fieldDTO.getFieldImg1())
                .fieldImg2(fieldDTO.getFieldImg2())
                .build();
        fieldRepository.save(fields);
        return "Field saved successfully";
    }

    @Override
    public String updateField(FieldDTO fieldDTO){
        // Check if field exists before updating
        Field existingField = fieldRepository.findById(fieldDTO.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field with ID " + fieldDTO.getFieldId() + " does not exist"));

        // Check if field name is being changed to an existing name of a different field
        if (!existingField.getFieldName().equals(fieldDTO.getFieldName())) {
            if (fieldRepository.existsByFieldName(fieldDTO.getFieldName())) {
                throw new RuntimeException("Field name '" + fieldDTO.getFieldName() + "' is already in use");
            }
        }

        Field fields = Field.builder()
                .fieldId(fieldDTO.getFieldId())
                .fieldName(fieldDTO.getFieldName())
                .size(fieldDTO.getSize())
                .location(fieldDTO.getLocation())
                .fieldImg1(fieldDTO.getFieldImg1())
                .fieldImg2(fieldDTO.getFieldImg2())
                .build();
        fieldRepository.save(fields);
        return "Field updated successfully";
    }

    @Override
    public String deleteField(String fieldId){
        if (!fieldRepository.existsById(fieldId)) {
            throw new RuntimeException("Field with ID " + fieldId + " does not exist");
        }
        fieldRepository.deleteById(fieldId);
        return "Field deleted successfully";
    }

    @Override
    public List<FieldDTO> getAllFields(){
        return modelMapper.map(fieldRepository.findAll(), new TypeToken<List<FieldDTO>>() {}.getType());
    }

    private String generateFieldID() {
        if (fieldRepository.count() == 0) {
            return "F001";
        } else {
            String lastId = fieldRepository.findAll().get(fieldRepository.findAll().size() - 1).getFieldId();
            int newId = Integer.parseInt(lastId.substring(1)) + 1;
            if (newId < 10) {
                return "F00" + newId;
            } else if (newId < 100) {
                return "F0" + newId;
            } else {
                return "F" + newId;
            }
        }
    }

}
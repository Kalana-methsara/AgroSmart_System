package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.FieldDTO;

import java.util.List;

public interface FieldService {
    String saveField(FieldDTO fieldDTO);
    String updateField(FieldDTO fieldDTO);
    String deleteField(String fieldId);
    List<FieldDTO> getAllFields();
}
package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.FieldCropDTO;
import java.util.List;

public interface FieldCropService {
    String assignCropToField(FieldCropDTO fieldCropDTO);
    String updateAssignment(FieldCropDTO fieldCropDTO);
    String removeCropFromField(String fieldId, String cropId);
    List<FieldCropDTO> getCropsByField(String fieldId);
    List<FieldCropDTO> getFieldsByCrop(String cropId);
    List<FieldCropDTO> getAllAssignments();
}
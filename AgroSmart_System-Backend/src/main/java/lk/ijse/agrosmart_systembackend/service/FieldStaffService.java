package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.FieldStaffDTO;
import java.util.List;

public interface FieldStaffService {
    String assignStaffToField(FieldStaffDTO fieldStaffDTO);
    String updateAssignment(FieldStaffDTO fieldStaffDTO);
    String removeStaffFromField(String fieldId, String staffId);
    List<FieldStaffDTO> getStaffByField(String fieldId);
    List<FieldStaffDTO> getFieldsByStaff(String staffId);
    List<FieldStaffDTO> getAllAssignments();
}
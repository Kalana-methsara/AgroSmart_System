package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.StaffDTO;

import java.util.List;

public interface StaffService {

    String saveStaff(StaffDTO staffDTO);
    String updateStaff(StaffDTO staffDTO);
    String deleteStaff(String id);
    StaffDTO getStaff(String id);
    List<StaffDTO> getAllStaff();
}
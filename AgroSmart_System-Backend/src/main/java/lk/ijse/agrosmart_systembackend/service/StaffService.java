package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.StaffDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StaffService {


    List<StaffDTO> getAllStaff();
    List<StaffDTO> findActive();
    StaffDTO getStaff(String staffId);
    String saveStaff(StaffDTO staffDTO);
    String updateStaff(StaffDTO staffDTO);
    String deleteStaff(String staffId);
}
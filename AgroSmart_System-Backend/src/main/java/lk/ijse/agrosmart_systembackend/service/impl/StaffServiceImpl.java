package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.StaffDTO;
import lk.ijse.agrosmart_systembackend.entity.Staff;
import lk.ijse.agrosmart_systembackend.repository.StaffRepository;
import lk.ijse.agrosmart_systembackend.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<StaffDTO> getAllStaff() {
        return modelMapper.map(staffRepository.findAll(), new TypeToken<List<StaffDTO>>() {}.getType());
    }

    @Override
    public List<StaffDTO> findActive() {
        List<Staff> activeStaff = staffRepository.findByStatus(Staff.StaffStatus.ACTIVE);
        return modelMapper.map(activeStaff, new TypeToken<List<StaffDTO>>() {}.getType());
    }

    @Override
    public StaffDTO getStaff(String staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff with ID " + staffId + " not found"));
        return modelMapper.map(staff, StaffDTO.class);
    }

    @Override
    public String saveStaff(StaffDTO staffDTO) {
        if (staffRepository.existsById(staffDTO.getStaffId())) {
            throw new RuntimeException("Staff with ID " + staffDTO.getStaffId() + " already exists");
        }
        Staff staff = Staff.builder()
                .staffId(staffDTO.getStaffId())
                .fullName(staffDTO.getFullName())
                .designation(staffDTO.getDesignation())
                .position(staffDTO.getPosition())
                .bankName(staffDTO.getBankName())
                .accountNumber(staffDTO.getAccountNumber())
                .status(Staff.StaffStatus.ACTIVE)
                .build();

        staffRepository.save(staff);
        return "Staff saved successfully";
    }

    @Override
    public String updateStaff(StaffDTO staffDTO) {
        Staff existingStaff = staffRepository.findById(staffDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff with ID " + staffDTO.getStaffId() + " not found"));

        existingStaff.setFullName(staffDTO.getFullName());
        existingStaff.setDesignation(staffDTO.getDesignation());
        existingStaff.setPosition(staffDTO.getPosition());
        existingStaff.setBankName(staffDTO.getBankName());
        existingStaff.setAccountNumber(staffDTO.getAccountNumber());

        staffRepository.save(existingStaff);
        return "Staff updated successfully";
    }

    @Override
    public String deleteStaff(String staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff with ID " + staffId + " not found"));

        // Soft delete logic as per your original code
        staff.setStatus(Staff.StaffStatus.INACTIVE);
        staffRepository.save(staff);

        return "Staff deleted successfully";
    }
}
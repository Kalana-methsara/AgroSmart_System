package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.StaffDTO;
import lk.ijse.agrosmart_systembackend.entity.Staff;
import lk.ijse.agrosmart_systembackend.entity.enums.StaffStatus;
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
        List<Staff> activeStaff = staffRepository.findByStatus(StaffStatus.ACTIVE);
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
//        if (staffRepository.existsById(staffDTO.getStaffId())) {
//            throw new RuntimeException("Staff with ID " + staffDTO.getStaffId() + " already exists");
//        }
        Staff staff = Staff.builder()
                .staffId(generateStaffID())
                .fullName(staffDTO.getFullName())
                .contactNumber(staffDTO.getContactNumber())
                .email(staffDTO.getEmail())
                .role(staffDTO.getRole())
                .designation(staffDTO.getDesignation())
                .position(staffDTO.getPosition())
                .bankName(staffDTO.getBankName())
                .accountNumber(staffDTO.getAccountNumber())
                .status(StaffStatus.ACTIVE)
                .build();

        staffRepository.save(staff);
        return "Staff saved successfully";
    }

    @Override
    public String updateStaff(StaffDTO staffDTO) {
        Staff existingStaff = staffRepository.findById(staffDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff with ID " + staffDTO.getStaffId() + " not found"));

        existingStaff.setFullName(staffDTO.getFullName());
        existingStaff.setContactNumber(staffDTO.getContactNumber());
        existingStaff.setEmail(staffDTO.getEmail());
        existingStaff.setRole(staffDTO.getRole());
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

        staff.setStatus(StaffStatus.INACTIVE);
        staffRepository.save(staff);

        return "Staff deleted successfully";
    }

    private String generateStaffID() {
        List<Staff> staffList = staffRepository.findAll();

        if (staffList.isEmpty()) {
            return "STF-001";
        }

        String lastId = staffList.get(staffList.size() - 1).getStaffId();

        // STF-001 → 001
        String numberPart = lastId.split("-")[1];

        int newId = Integer.parseInt(numberPart) + 1;

        return String.format("STF-%03d", newId);
    }
}
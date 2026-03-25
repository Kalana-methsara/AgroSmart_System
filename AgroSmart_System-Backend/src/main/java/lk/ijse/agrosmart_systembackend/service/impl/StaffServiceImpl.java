package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.StaffDTO;
import lk.ijse.agrosmart_systembackend.entity.Staff;
import lk.ijse.agrosmart_systembackend.entity.Vehicle;
import lk.ijse.agrosmart_systembackend.repository.StaffRepository;
import lk.ijse.agrosmart_systembackend.repository.VehicleRepository;
import lk.ijse.agrosmart_systembackend.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;

    @Override
    public String saveStaff(StaffDTO staffDTO) {

        String newId = generateStaffID();

        if (staffRepository.existsById(newId)) {
            throw new RuntimeException("Generated Staff ID already exists: " + newId);
        }

        if (staffRepository.existsByEmail(staffDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (staffRepository.existsByContactNumber(staffDTO.getContactNumber())) {
            throw new RuntimeException("Contact number already exists");
        }

        Vehicle vehicle = null;
        if (staffDTO.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(staffDTO.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        }

        Staff staff = Staff.builder()
                .staffId(newId)
                .fullName(staffDTO.getFullName())
                .designation(staffDTO.getDesignation())
                .gender(staffDTO.getGender())
                .contactNumber(staffDTO.getContactNumber())
                .email(staffDTO.getEmail())
                .role(staffDTO.getRole())
                .vehicleId(vehicle)
                .build();

        staffRepository.save(staff);
        return "Staff saved successfully";
    }

    @Override
    public String updateStaff(StaffDTO staffDTO) {

        if (!staffRepository.existsById(staffDTO.getStaffId())) {
            throw new RuntimeException("Staff not found");
        }

        Staff existing = staffRepository.findById(staffDTO.getStaffId()).get();

        if (!existing.getEmail().equals(staffDTO.getEmail()) &&
                staffRepository.existsByEmail(staffDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (!existing.getContactNumber().equals(staffDTO.getContactNumber()) &&
                staffRepository.existsByContactNumber(staffDTO.getContactNumber())) {
            throw new RuntimeException("Contact number already exists");
        }

        Vehicle vehicle = null;
        if (staffDTO.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(staffDTO.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        }

        Staff staff = Staff.builder()
                .staffId(staffDTO.getStaffId())
                .fullName(staffDTO.getFullName())
                .designation(staffDTO.getDesignation())
                .gender(staffDTO.getGender())
                .contactNumber(staffDTO.getContactNumber())
                .email(staffDTO.getEmail())
                .role(staffDTO.getRole())
                .vehicleId(vehicle)
                .build();

        staffRepository.save(staff);
        return "Staff updated successfully";
    }

    @Override
    public String deleteStaff(String id) {

        if (!staffRepository.existsById(id)) {
            throw new RuntimeException("Staff not found");
        }

        staffRepository.deleteById(id);
        return "Staff deleted successfully";
    }

    @Override
    public StaffDTO getStaff(String id) {

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        StaffDTO dto = modelMapper.map(staff, StaffDTO.class);

        if (staff.getVehicleId() != null) {
            dto.setVehicleId(staff.getVehicleId().getVehicleId());
        }

        return dto;
    }

    @Override
    public List<StaffDTO> getAllStaff() {

        List<StaffDTO> list = modelMapper.map(
                staffRepository.findAll(),
                new TypeToken<List<StaffDTO>>() {}.getType()
        );

        list.forEach(dto -> {
            Staff staff = staffRepository.findById(dto.getStaffId()).orElse(null);
            if (staff != null && staff.getVehicleId() != null) {
                dto.setVehicleId(staff.getVehicleId().getVehicleId());
            }
        });

        return list;
    }

    private String generateStaffID() {
        List<Staff> staffList = staffRepository.findAll();

        if (staffList.isEmpty()) {
            return "S001";
        }

        String lastId = staffList.get(staffList.size() - 1).getStaffId();
        int newId = Integer.parseInt(lastId.substring(1)) + 1;

        return String.format("S%03d", newId);
    }
}

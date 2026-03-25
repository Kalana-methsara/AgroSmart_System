package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.EquipmentDTO;
import lk.ijse.agrosmart_systembackend.entity.Equipment;
import lk.ijse.agrosmart_systembackend.entity.Field;
import lk.ijse.agrosmart_systembackend.entity.Staff;
import lk.ijse.agrosmart_systembackend.repository.EquipmentRepository;
import lk.ijse.agrosmart_systembackend.repository.FieldRepository;
import lk.ijse.agrosmart_systembackend.repository.StaffRepository;
import lk.ijse.agrosmart_systembackend.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final FieldRepository fieldRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Override
    public String saveEquipment(EquipmentDTO equipmentDTO) {
        Optional<Equipment> existingEquipment = equipmentRepository
                .findByCategoryAndType(equipmentDTO.getCategory(), equipmentDTO.getType());

        if (existingEquipment.isPresent()) {
            return "Equipment with same name and type already exists";
        } else {
            String newId = generateEquipmentId();
            Field field = fieldRepository.findById(equipmentDTO.getFieldId())
                    .orElse(null);
            Staff staff = staffRepository.findById(equipmentDTO.getStaffId())
                    .orElse(null);

            Equipment equipment = Equipment.builder()
                    .equipmentId(newId)
                    .category(equipmentDTO.getCategory())
                    .type(equipmentDTO.getType())
                    .status(equipmentDTO.getStatus())
                    .field(field)
                    .staff(staff)
                    .build();

            equipmentRepository.save(equipment);
            return "New equipment saved successfully";
        }
    }

    @Override
    public String updateEquipment(EquipmentDTO equipmentDTO) {
        if (!equipmentRepository.existsById(equipmentDTO.getEquipmentId())) {
            throw new RuntimeException("Equipment with ID " + equipmentDTO.getEquipmentId() + " does not exist");
        }

        Field field = fieldRepository.findById(equipmentDTO.getFieldId())
                .orElse(null);
        Staff staff = staffRepository.findById(equipmentDTO.getStaffId())
                .orElse(null);

        Equipment equipment = Equipment.builder()
                .equipmentId(equipmentDTO.getEquipmentId())
                .category(equipmentDTO.getCategory())
                .type(equipmentDTO.getType())
                .status(equipmentDTO.getStatus())
                .field(field)
                .staff(staff)
                .build();

        equipmentRepository.save(equipment);
        return "Equipment updated successfully";
    }

    @Override
    public String deleteEquipment(String equipmentId) {
        if (!equipmentRepository.existsById(equipmentId)) {
            throw new RuntimeException("Equipment with ID " + equipmentId + " does not exist");
        }
        equipmentRepository.deleteById(equipmentId);
        return "Equipment deleted successfully";
    }

    @Override
    public List<EquipmentDTO> getAllEquipments() {
        return modelMapper.map(equipmentRepository.findAll(), new TypeToken<List<EquipmentDTO>>() {}.getType());
    }

    private String generateEquipmentId() {
        List<Equipment> equipments = equipmentRepository.findAll();
        if (equipments.isEmpty()) {
            return "E001";
        }
        String lastId = equipments.get(equipments.size() - 1).getEquipmentId();
        int newId = Integer.parseInt(lastId.substring(1)) + 1;
        return String.format("E%03d", newId);
    }
}
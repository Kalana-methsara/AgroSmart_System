package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.EquipmentDTO;

import java.util.List;

public interface EquipmentService {
    String saveEquipment(EquipmentDTO equipmentDTO);
    String updateEquipment(EquipmentDTO equipmentDTO);
    String deleteEquipment(String equipmentId);
    List<EquipmentDTO> getAllEquipments();
}
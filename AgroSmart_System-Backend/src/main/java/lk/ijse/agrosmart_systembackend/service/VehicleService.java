package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.VehicleDTO;

import java.util.List;

public interface VehicleService {
    String saveVehicle(VehicleDTO vehicleDTO);
    String updateVehicle(VehicleDTO vehicleDTO);
    String deleteVehicle(String vehicleId);
    List<VehicleDTO> getAllVehicles();
}

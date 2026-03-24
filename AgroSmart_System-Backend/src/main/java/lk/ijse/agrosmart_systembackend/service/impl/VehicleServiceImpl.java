package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.FieldDTO;
import lk.ijse.agrosmart_systembackend.dto.VehicleDTO;
import lk.ijse.agrosmart_systembackend.entity.Field;
import lk.ijse.agrosmart_systembackend.entity.Vehicle;
import lk.ijse.agrosmart_systembackend.repository.VehicleRepository;
import lk.ijse.agrosmart_systembackend.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;

    private final ModelMapper modelMapper;

    @Override
    public String saveVehicle(VehicleDTO vehicleDTO) {
        String newId = generateVehicleID();
        if (vehicleRepository.existsById(newId)) {
            throw new RuntimeException("Generated Vehicle ID already exists: " + newId);
        }
        if (vehicleRepository.existsByNumberPlate(vehicleDTO.getNumberPlate())) {
            throw new RuntimeException("Vehicle with number plate " + vehicleDTO.getNumberPlate() + " already exists");
        }
        Vehicle vehicle = Vehicle.builder()
                .vehicleId(newId)
                .category(vehicleDTO.getCategory())
                .fuelType(vehicleDTO.getFuelType())
                .numberPlate(vehicleDTO.getNumberPlate())
                .remarks(vehicleDTO.getRemarks())
                .status(vehicleDTO.getStatus())
                .build();
        vehicleRepository.save(vehicle);
        return "Vehicle saved successfully";
    }

    @Override
    public String updateVehicle(VehicleDTO vehicleDTO) {
        System.out.println(vehicleDTO.getVehicleId());
        if (!vehicleRepository.existsById(vehicleDTO.getVehicleId())) {
            throw new RuntimeException("Vehicle with ID " + vehicleDTO.getVehicleId() + " does not exist");
        }
        Vehicle existing = vehicleRepository.findById(vehicleDTO.getVehicleId()).get();

        if (!existing.getNumberPlate().equals(vehicleDTO.getNumberPlate()) &&
                vehicleRepository.existsByNumberPlate(vehicleDTO.getNumberPlate())) {
            throw new RuntimeException("Vehicle with number plate " + vehicleDTO.getNumberPlate() + " already exists");
        }
        Vehicle vehicle = Vehicle.builder()
                .vehicleId(vehicleDTO.getVehicleId())
                .category(vehicleDTO.getCategory())
                .fuelType(vehicleDTO.getFuelType())
                .numberPlate(vehicleDTO.getNumberPlate())
                .remarks(vehicleDTO.getRemarks())
                .status(vehicleDTO.getStatus())
                .build();
        vehicleRepository.save(vehicle);
        return "Vehicle updated successfully";
    }

    @Override
    public String deleteVehicle(String vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new RuntimeException("Vehicle with ID " + vehicleId + " does not exist");
        }
        vehicleRepository.deleteById(vehicleId);
        return "Vehicle deleted successfully";    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        return modelMapper.map(vehicleRepository.findAll(), new TypeToken<List<VehicleDTO>>() {}.getType());
    }
    private String generateVehicleID() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            return "V001";
        }
        String lastId = vehicles.get(vehicles.size() - 1).getVehicleId();
        int newId = Integer.parseInt(lastId.substring(1)) + 1;

        return String.format("V%03d", newId);
    }
}

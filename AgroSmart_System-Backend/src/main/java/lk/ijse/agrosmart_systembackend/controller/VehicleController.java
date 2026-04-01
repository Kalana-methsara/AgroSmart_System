package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.VehicleDTO;
import lk.ijse.agrosmart_systembackend.entity.SalarySetting;
import lk.ijse.agrosmart_systembackend.entity.enums.SettingType;
import lk.ijse.agrosmart_systembackend.entity.enums.ValueType;
import lk.ijse.agrosmart_systembackend.repository.SalarySettingRepository;
import lk.ijse.agrosmart_systembackend.service.VehicleService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveVehicle(@RequestBody VehicleDTO vehicleDTO){
        return new ResponseEntity<>(new ApiResponse(201,"Vehicle Saved Successfully",vehicleService.saveVehicle(vehicleDTO)), HttpStatus.CREATED);
    }

    // GET ALL
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllVehicles() {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", vehicleService.getAllVehicles()),
                HttpStatus.OK
        );
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<ApiResponse> updateVehicle(@RequestBody VehicleDTO vehicleDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Vehicle Updated Successfully", vehicleService.updateVehicle(vehicleDTO)),
                HttpStatus.OK
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteVehicle(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Vehicle Deleted Successfully", vehicleService.deleteVehicle(id)),
                HttpStatus.OK
        );
    }
}

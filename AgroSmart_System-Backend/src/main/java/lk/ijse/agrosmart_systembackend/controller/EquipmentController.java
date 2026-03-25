package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.EquipmentDTO;
import lk.ijse.agrosmart_systembackend.service.EquipmentService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse> saveEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        return new ResponseEntity<>(
                new ApiResponse(201, "Equipment Saved Successfully", equipmentService.saveEquipment(equipmentDTO)),
                HttpStatus.CREATED
        );
    }

    // GET ALL
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllEquipments() {
        List<EquipmentDTO> equipments = equipmentService.getAllEquipments();
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", equipments),
                HttpStatus.OK
        );
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<ApiResponse> updateEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Equipment Updated Successfully", equipmentService.updateEquipment(equipmentDTO)),
                HttpStatus.OK
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEquipment(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Equipment Deleted Successfully", equipmentService.deleteEquipment(id)),
                HttpStatus.OK
        );
    }
}
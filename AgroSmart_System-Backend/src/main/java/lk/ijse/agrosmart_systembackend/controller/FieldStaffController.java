package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.FieldStaffDTO;
import lk.ijse.agrosmart_systembackend.service.FieldStaffService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/field-staff")
@RequiredArgsConstructor
@CrossOrigin
public class FieldStaffController {

    private final FieldStaffService fieldStaffService;

    @PostMapping
    public ResponseEntity<ApiResponse> assignStaffToField(@RequestBody FieldStaffDTO fieldStaffDTO) {
        String result = fieldStaffService.assignStaffToField(fieldStaffDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", result));
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateAssignment(@RequestBody FieldStaffDTO fieldStaffDTO) {
        String result = fieldStaffService.updateAssignment(fieldStaffDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", result));
    }

    @DeleteMapping("/{fieldId}/{staffId}")
    public ResponseEntity<ApiResponse> removeStaffFromField(
            @PathVariable String fieldId,
            @PathVariable String staffId) {
        String result = fieldStaffService.removeStaffFromField(fieldId, staffId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", result));
    }

    @GetMapping("/field/{fieldId}")
    public ResponseEntity<ApiResponse> getStaffByField(@PathVariable String fieldId) {
        List<FieldStaffDTO> assignments = fieldStaffService.getStaffByField(fieldId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", assignments));
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<ApiResponse> getFieldsByStaff(@PathVariable String staffId) {
        List<FieldStaffDTO> assignments = fieldStaffService.getFieldsByStaff(staffId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", assignments));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAssignments() {
        List<FieldStaffDTO> assignments = fieldStaffService.getAllAssignments();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", assignments));
    }
}
package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.FieldCropDTO;
import lk.ijse.agrosmart_systembackend.service.FieldCropService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/field-crop")
@RequiredArgsConstructor
@CrossOrigin
public class FieldCropController {

    private final FieldCropService fieldCropService;

    @PostMapping
    public ResponseEntity<ApiResponse> assignCropToField(@RequestBody FieldCropDTO fieldCropDTO) {
        String result = fieldCropService.assignCropToField(fieldCropDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", result));
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateAssignment(@RequestBody FieldCropDTO fieldCropDTO) {
        String result = fieldCropService.updateAssignment(fieldCropDTO);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", result));
    }

    @DeleteMapping("/{fieldId}/{cropId}")
    public ResponseEntity<ApiResponse> removeCropFromField(
            @PathVariable String fieldId,
            @PathVariable String cropId) {
        String result = fieldCropService.removeCropFromField(fieldId, cropId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", result));
    }

    @GetMapping("/field/{fieldId}")
    public ResponseEntity<ApiResponse> getCropsByField(@PathVariable String fieldId) {
        List<FieldCropDTO> assignments = fieldCropService.getCropsByField(fieldId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", assignments));
    }

    @GetMapping("/crop/{cropId}")
    public ResponseEntity<ApiResponse> getFieldsByCrop(@PathVariable String cropId) {
        List<FieldCropDTO> assignments = fieldCropService.getFieldsByCrop(cropId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", assignments));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAssignments() {
        List<FieldCropDTO> assignments = fieldCropService.getAllAssignments();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", assignments));
    }
}
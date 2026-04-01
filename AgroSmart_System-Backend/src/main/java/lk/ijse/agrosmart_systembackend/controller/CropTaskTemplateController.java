package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.CropTaskTemplateDTO;
import lk.ijse.agrosmart_systembackend.service.CropTaskTemplateService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/crop-task-template")
@RequiredArgsConstructor
public class CropTaskTemplateController {

    private final CropTaskTemplateService templateService;

    // --- SEARCH BY CROP ID ---
    @GetMapping("/search/{cropId}")
    public ResponseEntity<ApiResponse> getTemplatesByCropId(@PathVariable String cropId) {
        List<CropTaskTemplateDTO> templates = templateService.getTemplatesByCropId(cropId);
        return new ResponseEntity<>(
                new ApiResponse(200, "Templates retrieved successfully for crop: " + cropId, templates),
                HttpStatus.OK
        );
    }

    // --- OTHER ENDPOINTS ---
    @GetMapping
    public ResponseEntity<ApiResponse> getAllTemplates() {
        List<CropTaskTemplateDTO> templates = templateService.getAllTemplates();
        return new ResponseEntity<>(
                new ApiResponse(200, "All templates retrieved successfully", templates),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTemplate(@RequestBody CropTaskTemplateDTO dto) {
        String message = templateService.saveTemplate(dto);
        return new ResponseEntity<>(
                new ApiResponse(201, message, null),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateTemplate(@RequestBody CropTaskTemplateDTO dto) {
        String message = templateService.updateTemplate(dto);
        return new ResponseEntity<>(
                new ApiResponse(200, message, null),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<ApiResponse> deleteTemplate(@PathVariable String templateId) {
        String message = templateService.deleteTemplate(templateId);
        return new ResponseEntity<>(
                new ApiResponse(200, message, null),
                HttpStatus.OK
        );
    }
}
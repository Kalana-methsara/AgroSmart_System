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

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse> saveTemplate(@RequestBody CropTaskTemplateDTO dto) {
        return new ResponseEntity<>(
                new ApiResponse(201, "Template Saved Successfully", templateService.saveTemplate(dto)),
                HttpStatus.CREATED
        );
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<ApiResponse> updateTemplate(@RequestBody CropTaskTemplateDTO dto) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Template Updated Successfully", templateService.updateTemplate(dto)),
                HttpStatus.OK
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTemplate(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Template Deleted Successfully", templateService.deleteTemplate(id)),
                HttpStatus.OK
        );
    }

    // GET ALL
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTemplates() {
        List<CropTaskTemplateDTO> templates = templateService.getAllTemplates();
        return new ResponseEntity<>(
                new ApiResponse(200, "Templates Retrieved Successfully", templates),
                HttpStatus.OK
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTemplateById(@PathVariable String id) {
        CropTaskTemplateDTO dto = templateService.getTemplateById(id);
        return new ResponseEntity<>(
                new ApiResponse(200, "Template Found", dto),
                HttpStatus.OK
        );
    }

    @GetMapping("/search/{cropId}")
    public ResponseEntity<ApiResponse> searchByTaskName(@PathVariable String cropId) {
        System.out.println(cropId);
        List<CropTaskTemplateDTO> results = templateService.getTemplatesByCropId(cropId);
        return new ResponseEntity<>(
                new ApiResponse(200, "Search Results", results),
                HttpStatus.OK
        );
    }
}
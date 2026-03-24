package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.FieldDTO;
import lk.ijse.agrosmart_systembackend.service.FieldService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/field")
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;

    // SAVE
    @PostMapping
    public ResponseEntity<ApiResponse> saveField(@RequestBody FieldDTO fieldDTO) {
        return new ResponseEntity<>(
                new ApiResponse(201, "Field Saved Successfully", fieldService.saveField(fieldDTO)),
                HttpStatus.CREATED
        );
    }

    // GET ALL
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllFields() {
        List<FieldDTO> fields = fieldService.getAllFields();
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", fields),
                HttpStatus.OK
        );
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<ApiResponse> updateField(@RequestBody FieldDTO fieldDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Field Updated Successfully",fieldService.updateField(fieldDTO)),
                HttpStatus.OK
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteField(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Field Deleted Successfully",fieldService.deleteField(id)),
                HttpStatus.OK
        );
    }

}
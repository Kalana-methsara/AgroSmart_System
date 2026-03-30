package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.FieldRequestDTO;
import lk.ijse.agrosmart_systembackend.service.FieldRequestService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/farming-log")
@RequiredArgsConstructor
public class FieldRequestController {

    private final FieldRequestService fieldRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveLog(@RequestBody FieldRequestDTO fieldRequestDTO){
        return new ResponseEntity<>(
                new ApiResponse(201,"Log Saved Successfully", fieldRequestService.saveField(fieldRequestDTO)),
                HttpStatus.CREATED
        );
    }
}

package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.StaffDTO;
import lk.ijse.agrosmart_systembackend.service.StaffService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    //  CREATE
    @PostMapping
    public ResponseEntity<ApiResponse> saveStaff(@RequestBody StaffDTO staffDTO) {
        return new ResponseEntity<>(
                new ApiResponse(201, "Staff Saved Successfully", staffService.saveStaff(staffDTO)),
                HttpStatus.CREATED
        );
    }

    //  GET ALL
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllStaff() {
        List<StaffDTO> staffList = staffService.getAllStaff();
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", staffList),
                HttpStatus.OK
        );
    }

    //  GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getStaff(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", staffService.getStaff(id)),
                HttpStatus.OK
        );
    }

    //  UPDATE
    @PutMapping
    public ResponseEntity<ApiResponse> updateStaff(@RequestBody StaffDTO staffDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Staff Updated Successfully", staffService.updateStaff(staffDTO)),
                HttpStatus.OK
        );
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStaff(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Staff Deleted Successfully", staffService.deleteStaff(id)),
                HttpStatus.OK
        );
    }
}
package lk.ijse.agrosmart_systembackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("api/v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    // SAVE STAFF
    @PostMapping
    public ResponseEntity<ApiResponse> saveStaff(@Valid @RequestBody StaffDTO staffDTO) {
        return ResponseEntity.ok(new ApiResponse(
                200, "Staff Saved Successfully", staffService.saveStaff(staffDTO))
        );
    }

    // GET ALL STAFF
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllStaff() {
        List<StaffDTO> staffList = staffService.getAllStaff();
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", staffList),
                HttpStatus.OK
        );
    }

    // GET STAFF BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getStaff(@PathVariable String id) {
        StaffDTO staff = staffService.getStaff(id);
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", staff),
                HttpStatus.OK
        );
    }

    // UPDATE STAFF
    @PutMapping
    public ResponseEntity<ApiResponse> updateStaff(@Valid @RequestBody StaffDTO staffDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Staff Updated Successfully", staffService.updateStaff(staffDTO)),
                HttpStatus.OK
        );
    }

    // DELETE STAFF
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStaff(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Staff Deleted Successfully", staffService.deleteStaff(id)),
                HttpStatus.OK
        );
    }
    // GET ACTIVE STAFF
    @GetMapping("/active")
    public ResponseEntity<ApiResponse> getActiveStaff() {
        List<StaffDTO> activeStaff = staffService.findActive();
        return new ResponseEntity<>(
                new ApiResponse(200, "Active staff retrieved successfully", activeStaff),
                HttpStatus.OK
        );
    }
}
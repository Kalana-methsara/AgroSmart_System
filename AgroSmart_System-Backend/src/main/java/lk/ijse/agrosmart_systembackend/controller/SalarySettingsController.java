package lk.ijse.agrosmart_systembackend.controller;

import jakarta.validation.Valid;
import lk.ijse.agrosmart_systembackend.dto.SalaryGradeDTO;
import lk.ijse.agrosmart_systembackend.dto.SalarySettingDTO;
import lk.ijse.agrosmart_systembackend.entity.enums.SettingType;
import lk.ijse.agrosmart_systembackend.service.SalaryGradeService;
import lk.ijse.agrosmart_systembackend.service.SalarySettingService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/salary-settings")
@RequiredArgsConstructor
public class SalarySettingsController {

    private final SalarySettingService salarySettingService;
    private final SalaryGradeService salaryGradeService;


    // GET ALL SETTINGS
    @GetMapping
    public ResponseEntity<ApiResponse> getAllSettings(@RequestParam(required = false) String type) {
        List<SalarySettingDTO> settings = type != null
                ? salarySettingService.findByType(SettingType.valueOf(type.toUpperCase()))
                : salarySettingService.findAll();

        return new ResponseEntity<>(
                new ApiResponse(200, "Success", settings),
                HttpStatus.OK
        );
    }

    // GET SETTING BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSetting(@PathVariable Long id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", salarySettingService.findById(id)),
                HttpStatus.OK
        );
    }

    // CREATE SETTING
    @PostMapping
    public ResponseEntity<ApiResponse> createSetting(@Valid @RequestBody SalarySettingDTO salarySettingDTO) {
        return new ResponseEntity<>(
                new ApiResponse(201, "Salary Setting Created Successfully", salarySettingService.create(salarySettingDTO)),
                HttpStatus.CREATED
        );
    }

    // UPDATE SETTING
    @PutMapping
    public ResponseEntity<ApiResponse> updateSetting(@Valid @RequestBody SalarySettingDTO salarySettingDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Salary Setting Updated Successfully", salarySettingService.update(salarySettingDTO)),
                HttpStatus.OK
        );
    }

    // DELETE SETTING
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSetting(@PathVariable Long id) {
        salarySettingService.delete(id);
        return new ResponseEntity<>(
                new ApiResponse(200, "Salary Setting Deleted Successfully", null),
                HttpStatus.OK
        );
    }

    // GET ALL GRADES
    @GetMapping("/grades")
    public ResponseEntity<ApiResponse> getAllGrades() {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", salaryGradeService.findAll()),
                HttpStatus.OK
        );
    }

    // GET GRADE BY ID
    @GetMapping("/grades/{id}")
    public ResponseEntity<ApiResponse> getGrade(@PathVariable Long id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", salaryGradeService.findById(id)),
                HttpStatus.OK
        );
    }

    // CREATE GRADE
    @PostMapping("/grades")
    public ResponseEntity<ApiResponse> createGrade(@Valid @RequestBody SalaryGradeDTO salaryGradeDTO) {
        return new ResponseEntity<>(
                new ApiResponse(201, "Salary Grade Created Successfully", salaryGradeService.create(salaryGradeDTO)),
                HttpStatus.CREATED
        );
    }

    // UPDATE GRADE
    @PutMapping("/grades/{id}")
    public ResponseEntity<ApiResponse> updateGrade(@Valid @RequestBody SalaryGradeDTO salaryGradeDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Salary Grade Updated Successfully", salaryGradeService.update(salaryGradeDTO)),
                HttpStatus.OK
        );
    }

    // DELETE GRADE
    @DeleteMapping("/grades/{id}")
    public ResponseEntity<ApiResponse> deleteGrade(@PathVariable Long id) {
        salaryGradeService.delete(id);
        return new ResponseEntity<>(
                new ApiResponse(200, "Salary Grade Deleted Successfully", null),
                HttpStatus.OK
        );
    }
}
package lk.ijse.agrosmart_systembackend.controller;

import jakarta.validation.Valid;
import lk.ijse.agrosmart_systembackend.dto.SalaryRecordDTO;
import lk.ijse.agrosmart_systembackend.service.impl.PdfServiceImpl;
import lk.ijse.agrosmart_systembackend.service.impl.SalaryRecordServiceImpl;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/salary-records")
@RequiredArgsConstructor
public class SalaryRecordController {

    private final SalaryRecordServiceImpl salaryRecordService;
    private final PdfServiceImpl pdfService;

    // SEARCH SALARY RECORDS
    @GetMapping
    public ResponseEntity<ApiResponse> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String department) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", salaryRecordService.search(search, status, department)),
                HttpStatus.OK
        );
    }

    // GET BY PERIOD
    @GetMapping("/period")
    public ResponseEntity<ApiResponse> getByPeriod(
            @RequestParam int month,
            @RequestParam int year) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", salaryRecordService.findByPeriod(month, year)),
                HttpStatus.OK
        );
    }

    // GET BY EMPLOYEE
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse> getByEmployee(@PathVariable String employeeId) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", salaryRecordService.findByEmployee(employeeId)),
                HttpStatus.OK
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", salaryRecordService.findById(id)),
                HttpStatus.OK
        );
    }

    // CREATE RECORD
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody SalaryRecordDTO salaryRecordDTO) {
        return new ResponseEntity<>(
                new ApiResponse(201, "Salary Record Created Successfully", salaryRecordService.create(salaryRecordDTO)),
                HttpStatus.CREATED
        );
    }

    // UPDATE RECORD
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SalaryRecordDTO salaryRecordDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Salary Record Updated Successfully", salaryRecordService.update(salaryRecordDTO)),
                HttpStatus.OK
        );
    }

    // UPDATE STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null || status.isBlank()) {
            return new ResponseEntity<>(
                    new ApiResponse(400, "Field 'status' is required", null),
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                new ApiResponse(200, "Status Updated Successfully", salaryRecordService.updateStatus(id, status)),
                HttpStatus.OK
        );
    }

    // DELETE RECORD
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        salaryRecordService.delete(id);
        return new ResponseEntity<>(
                new ApiResponse(200, "Salary Record Deleted Successfully", null),
                HttpStatus.OK
        );
    }

    // DOWNLOAD PAYSLIP
    @GetMapping("/{id}/payslip")
    public ResponseEntity<byte[]> downloadPayslip(@PathVariable Long id) {
        try {
            byte[] pdf = pdfService.generatePayslip(id);
            String filename = "payslip-" + id + ".pdf";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
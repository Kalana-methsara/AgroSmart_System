package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.service.PayrollService;
import lk.ijse.agrosmart_systembackend.service.PdfService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;
    private final PdfService pdfService;

    // GET PAYROLL SUMMARY
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse> getSummary(
            @RequestParam int month,
            @RequestParam int year) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", payrollService.getSummary(month, year)),
                HttpStatus.OK
        );
    }

    // CALCULATE PAYROLL
    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse> calculate(
            @RequestParam int month,
            @RequestParam int year) {
        // Result is now a String message
        String result = payrollService.calculatePayroll(month, year);
        return new ResponseEntity<>(
                new ApiResponse(200, "Payroll Calculation Success", result),
                HttpStatus.OK
        );
    }

    // PROCESS PAYMENTS
    @PostMapping("/process")
    public ResponseEntity<ApiResponse> process(
            @RequestParam int month,
            @RequestParam int year) {
        // Result is now a String message
        String result = payrollService.processPayments(month, year);
        return new ResponseEntity<>(
                new ApiResponse(200, "Payment Processing Success", result),
                HttpStatus.OK
        );
    }

    // DOWNLOAD PAYROLL REPORT
    @GetMapping("/report")
    public ResponseEntity<byte[]> downloadReport(
            @RequestParam int month,
            @RequestParam int year) {
        try {
            byte[] pdf = pdfService.generatePayrollReport(month, year);
            String filename = String.format("payroll-report-%04d-%02d.pdf", year, month);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
package lk.ijse.agrosmart_systembackend.service;

import com.itextpdf.text.DocumentException;
import java.io.IOException;

public interface PdfService {

    byte[] generatePayslip(Long salaryRecordId) throws DocumentException, IOException;

    byte[] generatePayrollReport(int month, int year) throws DocumentException, IOException;
}
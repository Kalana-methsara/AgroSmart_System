package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.PayrollSummaryDTO;
import lk.ijse.agrosmart_systembackend.dto.SalaryRecordDTO;

import java.util.List;

public interface PayrollService {
    PayrollSummaryDTO getSummary(int month, int year);
    String calculatePayroll(int month, int year);
    String processPayments(int month, int year);
}
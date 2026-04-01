package lk.ijse.agrosmart_systembackend.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import lk.ijse.agrosmart_systembackend.entity.SalaryRecord;
import lk.ijse.agrosmart_systembackend.repository.SalaryRecordRepository;
import lk.ijse.agrosmart_systembackend.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final SalaryRecordRepository salaryRecordRepo;

    // Custom Branding Colors (Green theme for AgroSmart)
    private static final BaseColor PRIMARY_COLOR = new BaseColor(21, 128, 61); // Green-700
    private static final BaseColor SECONDARY_COLOR = new BaseColor(220, 252, 231); // Green-100
    private static final BaseColor TEXT_DARK = new BaseColor(17, 24, 39);
    private static final BaseColor TEXT_GRAY = new BaseColor(75, 85, 99);

    // Fonts
    private static final Font TITLE_FONT   = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, PRIMARY_COLOR);
    private static final Font HEADER_FONT  = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
    private static final Font LABEL_FONT   = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL, TEXT_GRAY);
    private static final Font VALUE_FONT   = new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD, TEXT_DARK);
    private static final Font TOTAL_FONT   = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, PRIMARY_COLOR);
    private static final Font NET_FONT     = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, PRIMARY_COLOR);
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(55, 65, 81));

    private static final BaseColor ROW_ALT     = new BaseColor(249, 250, 251);
    private static final BaseColor BORDER_COLOR = new BaseColor(229, 231, 235);

    public byte[] generatePayslip(Long salaryRecordId) throws DocumentException, IOException {
        SalaryRecord record = salaryRecordRepo.findById(salaryRecordId)
                .orElseThrow(() -> new RuntimeException("Salary Record not found with ID: " + salaryRecordId));
        return buildPayslipPdf(record);
    }

    public byte[] generatePayrollReport(int month, int year) throws DocumentException, IOException {
        List<SalaryRecord> records = salaryRecordRepo.findByPayrollMonthAndPayrollYear(month, year);
        return buildPayrollReportPdf(records, month, year);
    }

    private byte[] buildPayslipPdf(SalaryRecord r) throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4, 40, 40, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);
        doc.open();

        // Header Banner
        PdfPTable banner = new PdfPTable(1);
        banner.setWidthPercentage(100);
        PdfPCell bannerCell = new PdfPCell();
        bannerCell.setBackgroundColor(PRIMARY_COLOR);
        bannerCell.setPadding(16);
        bannerCell.setBorder(Rectangle.NO_BORDER);

        Paragraph title = new Paragraph("AgroSmart System", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.WHITE));
        title.setAlignment(Element.ALIGN_CENTER);
        Paragraph sub = new Paragraph("OFFICIAL PAYSLIP", new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, SECONDARY_COLOR));
        sub.setAlignment(Element.ALIGN_CENTER);

        bannerCell.addElement(title);
        bannerCell.addElement(sub);
        banner.addCell(bannerCell);
        doc.add(banner);

        doc.add(Chunk.NEWLINE);

        // Period Info
        String monthName = java.time.Month.of(r.getPayrollMonth()).getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH);
        addSectionTitle(doc, "Payroll Period: " + monthName + " " + r.getPayrollYear());

        // Employee details
        PdfPTable empTable = new PdfPTable(4);
        empTable.setWidthPercentage(100);
        empTable.setWidths(new float[]{1.5f, 2.5f, 1.5f, 2.5f});
        empTable.setSpacingBefore(6);
        empTable.setSpacingAfter(10);

        addDetailRow(empTable, "Employee Name", r.getStaff().getFullName(), "Staff ID", r.getStaff().getStaffId());
        addDetailRow(empTable, "Designation", r.getStaff().getDesignation(), "Payment Method", r.getPaymentMethod().toString());
        addDetailRow(empTable, "Bank Name", nvl(r.getStaff().getBankName()), "Account No", nvl(r.getStaff().getAccountNumber()));
        addDetailRow(empTable, "Payment Date", r.getPaymentDate() != null ? r.getPaymentDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) : "PENDING", "Status", r.getStatus().toString());
        doc.add(empTable);

        // Earnings & Deductions Logic
        addSectionTitle(doc, "Earnings");
        doc.add(buildTwoColTable(new String[]{"Basic Salary", "Allowances", "Total Earnings"},
                new BigDecimal[]{r.getBasicSalary(), r.getAllowances(), r.getBasicSalary().add(r.getAllowances())}, true));

        doc.add(Chunk.NEWLINE);
        addSectionTitle(doc, "Deductions");
        doc.add(buildTwoColTable(new String[]{"Total Deductions"}, new BigDecimal[]{r.getDeductions()}, false));

        doc.add(Chunk.NEWLINE);

        // Net Salary
        PdfPTable netTable = new PdfPTable(2);
        netTable.setWidthPercentage(100);
        netTable.setWidths(new float[]{3f, 2f});

        PdfPCell netLabel = new PdfPCell(new Phrase("NET SALARY PAYABLE", NET_FONT));
        netLabel.setBackgroundColor(SECONDARY_COLOR);
        netLabel.setPadding(12);
        netLabel.setBorder(Rectangle.NO_BORDER);
        netLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell netValue = new PdfPCell(new Phrase(fmt(r.getNetSalary()), NET_FONT));
        netValue.setBackgroundColor(SECONDARY_COLOR);
        netValue.setPadding(12);
        netValue.setBorder(Rectangle.NO_BORDER);
        netValue.setHorizontalAlignment(Element.ALIGN_RIGHT);

        netTable.addCell(netLabel);
        netTable.addCell(netValue);
        doc.add(netTable);

        // Footer
        doc.add(Chunk.NEWLINE);
        Paragraph footer = new Paragraph("This is a computer-generated document. Generated by AgroSmart System.", new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC, TEXT_GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        doc.add(footer);

        doc.close();
        return out.toByteArray();
    }

    private byte[] buildPayrollReportPdf(List<SalaryRecord> records, int month, int year) throws DocumentException {
        Document doc = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);
        doc.open();

        Paragraph title = new Paragraph("AgroSmart System – Monthly Payroll Report", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);

        String monthName = java.time.Month.of(month).getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH);
        Paragraph period = new Paragraph("Period: " + monthName + " " + year, LABEL_FONT);
        period.setAlignment(Element.ALIGN_CENTER);
        doc.add(period);
        doc.add(Chunk.NEWLINE);

        // Detailed Table
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.5f, 2.5f, 2f, 1.5f, 1.5f, 1.5f, 1.5f});

        String[] headers = {"ID", "Name", "Designation", "Basic", "Allowances", "Deductions", "Net Salary"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, HEADER_FONT));
            cell.setBackgroundColor(PRIMARY_COLOR);
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        for (SalaryRecord r : records) {
            addTableCell(table, r.getStaff().getStaffId(), Element.ALIGN_CENTER);
            addTableCell(table, r.getStaff().getFullName(), Element.ALIGN_LEFT);
            addTableCell(table, r.getStaff().getDesignation(), Element.ALIGN_LEFT);
            addTableCell(table, fmt(r.getBasicSalary()), Element.ALIGN_RIGHT);
            addTableCell(table, fmt(r.getAllowances()), Element.ALIGN_RIGHT);
            addTableCell(table, fmt(r.getDeductions()), Element.ALIGN_RIGHT);
            addTableCell(table, fmt(r.getNetSalary()), Element.ALIGN_RIGHT);
        }
        doc.add(table);

        doc.close();
        return out.toByteArray();
    }

    // Helper Methods
    private void addSectionTitle(Document doc, String text) throws DocumentException {
        Paragraph p = new Paragraph(text, SECTION_FONT);
        p.setSpacingBefore(10);
        p.setSpacingAfter(5);
        doc.add(p);
    }

    private void addDetailRow(PdfPTable table, String l1, String v1, String l2, String v2) {
        table.addCell(createLabelCell(l1));
        table.addCell(createValueCell(v1));
        table.addCell(createLabelCell(l2));
        table.addCell(createValueCell(v2));
    }

    private PdfPCell createLabelCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, LABEL_FONT));
        cell.setPadding(6);
        cell.setBorderColor(BORDER_COLOR);
        return cell;
    }

    private PdfPCell createValueCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, VALUE_FONT));
        cell.setPadding(6);
        cell.setBorderColor(BORDER_COLOR);
        return cell;
    }

    private PdfPTable buildTwoColTable(String[] labels, BigDecimal[] values, boolean isEarnings) throws DocumentException {
        PdfPTable t = new PdfPTable(2);
        t.setWidthPercentage(100);
        t.setWidths(new float[]{3f, 2f});
        for (int i = 0; i < labels.length; i++) {
            boolean isTotal = i == labels.length - 1;
            t.addCell(createValueCell(labels[i]));
            PdfPCell vCell = createValueCell(fmt(values[i]));
            vCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            if (isTotal) vCell.setBackgroundColor(SECONDARY_COLOR);
            t.addCell(vCell);
        }
        return t;
    }

    private void addTableCell(PdfPTable table, String text, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, VALUE_FONT));
        cell.setPadding(6);
        cell.setHorizontalAlignment(align);
        cell.setBorderColor(BORDER_COLOR);
        table.addCell(cell);
    }

    private String fmt(BigDecimal value) {
        if (value == null) return "0.00";
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(value);
    }

    private String nvl(String s) {
        return (s == null || s.isEmpty()) ? "—" : s;
    }
}
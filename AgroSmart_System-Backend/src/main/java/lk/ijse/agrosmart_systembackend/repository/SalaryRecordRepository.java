package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.SalaryRecord;
import lk.ijse.agrosmart_systembackend.entity.enums.SalaryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRecordRepository extends JpaRepository<SalaryRecord, Long> {

    List<SalaryRecord> findByStaff_StaffId(String staffId);

    List<SalaryRecord> findByStatus(SalaryStatus status);

    List<SalaryRecord> findByPayrollMonthAndPayrollYear(int month, int year);

    List<SalaryRecord> findByStaff_DesignationAndPayrollMonthAndPayrollYear(
            String department, int month, int year);

    Optional<SalaryRecord> findByStaff_StaffIdAndPayrollMonthAndPayrollYear(
            String staffId, int month, int year);

    @Query("SELECT COUNT(r) FROM SalaryRecord r WHERE r.payrollMonth = :month AND r.payrollYear = :year")
    long countByPeriod(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(r.basicSalary), 0) FROM SalaryRecord r WHERE r.payrollMonth = :month AND r.payrollYear = :year")
    BigDecimal sumBasicSalaryByPeriod(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(r.deductions), 0) FROM SalaryRecord r WHERE r.payrollMonth = :month AND r.payrollYear = :year")
    BigDecimal sumDeductionsByPeriod(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(r.netSalary), 0) FROM SalaryRecord r WHERE r.payrollMonth = :month AND r.payrollYear = :year")
    BigDecimal sumNetSalaryByPeriod(@Param("month") int month, @Param("year") int year);

    @Query("""
        SELECT r FROM SalaryRecord r
        JOIN r.staff e
        WHERE (:search IS NULL OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                               OR LOWER(e.staffId) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:status IS NULL OR r.status = :status)
          AND (:department IS NULL OR e.designation = :department)
        ORDER BY r.createdAt DESC
    """)
    List<SalaryRecord> searchRecords(
            @Param("search") String search,
            @Param("status") SalaryStatus status,
            @Param("department") String department);
}

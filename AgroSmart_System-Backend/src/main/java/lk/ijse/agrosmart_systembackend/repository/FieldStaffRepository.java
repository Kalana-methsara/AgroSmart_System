package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.FieldCrop;
import lk.ijse.agrosmart_systembackend.entity.FieldStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldStaffRepository extends JpaRepository<FieldStaff, String> {

    List<FieldStaff> findByField_FieldId(String fieldId);

    List<FieldStaff> findByStaff_StaffId(String staffId);

    Optional<FieldStaff> findByField_FieldIdAndStaff_StaffId(String fieldId, String staffId);

    @Query("SELECT fs FROM FieldStaff fs WHERE fs.field.fieldId = :fieldId AND fs.assignedDate = CURRENT_DATE")
    List<FieldStaff> findCurrentAssignmentsByField(@Param("fieldId") String fieldId);

    void deleteByField_FieldIdAndStaff_StaffId(String fieldId, String staffId);
}
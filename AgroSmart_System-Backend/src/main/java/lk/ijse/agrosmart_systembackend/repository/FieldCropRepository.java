package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.FieldCrop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldCropRepository extends JpaRepository<FieldCrop, String> {

    List<FieldCrop> findByField_FieldId(String fieldId);

    List<FieldCrop> findByCrop_CropId(String cropId);

    Optional<FieldCrop> findByField_FieldIdAndCrop_CropId(String fieldId, String cropId);

    @Query("SELECT fc FROM FieldCrop fc WHERE fc.field.fieldId = :fieldId AND fc.assignedDate = CURRENT_DATE")
    List<FieldCrop> findCurrentCropsByField(@Param("fieldId") String fieldId);

    void deleteByField_FieldIdAndCrop_CropId(String fieldId, String cropId);
}
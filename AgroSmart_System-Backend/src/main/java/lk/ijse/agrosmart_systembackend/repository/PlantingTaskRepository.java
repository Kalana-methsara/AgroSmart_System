package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.PlantingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlantingTaskRepository extends JpaRepository<PlantingTask, String> {
    List<PlantingTask> findByFieldCrop_FieldCropId(String fieldCropId);
    List<PlantingTask> findByStatus(String status);
    List<PlantingTask> findByDueDateBeforeAndStatus(LocalDate date, String status); // ✅ Added
    long countByStatus(String status);
}

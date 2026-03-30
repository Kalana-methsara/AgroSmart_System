package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.PlantingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantingTaskRepository extends JpaRepository<PlantingTask, String> {
}

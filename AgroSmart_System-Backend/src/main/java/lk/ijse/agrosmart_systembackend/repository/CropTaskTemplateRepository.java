package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.dto.CropTaskTemplateDTO;
import lk.ijse.agrosmart_systembackend.entity.Crop;
import lk.ijse.agrosmart_systembackend.entity.CropTaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropTaskTemplateRepository extends JpaRepository<CropTaskTemplate,String> {
    List<CropTaskTemplate> findByCropId(String cropId);
}

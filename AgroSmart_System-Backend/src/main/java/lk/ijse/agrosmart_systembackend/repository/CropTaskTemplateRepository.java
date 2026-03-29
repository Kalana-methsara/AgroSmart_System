package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.dto.CropTaskTemplateDTO;
import lk.ijse.agrosmart_systembackend.entity.Crop;
import lk.ijse.agrosmart_systembackend.entity.CropTaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CropTaskTemplateRepository extends JpaRepository<CropTaskTemplate,String> {
    List<CropTaskTemplate> findByCropId(String cropId);
}

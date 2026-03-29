package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CropRepository extends JpaRepository<Crop, String> {

    boolean existsByCommonName(String commonName);
    boolean existsByScientificName(String scientificName);

}
package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.SalarySetting;
import lk.ijse.agrosmart_systembackend.entity.enums.SettingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalarySettingRepository extends JpaRepository<SalarySetting, Long> {
    List<SalarySetting> findByType(SettingType type);
    List<SalarySetting> findByTypeAndIsActive(SettingType type, Boolean isActive);
}

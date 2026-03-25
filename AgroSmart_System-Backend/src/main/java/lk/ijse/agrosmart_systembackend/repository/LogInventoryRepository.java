package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.Log;
import lk.ijse.agrosmart_systembackend.entity.LogInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogInventoryRepository extends JpaRepository<LogInventory, String> {
    List<LogInventory> findByLog(Log log);
}
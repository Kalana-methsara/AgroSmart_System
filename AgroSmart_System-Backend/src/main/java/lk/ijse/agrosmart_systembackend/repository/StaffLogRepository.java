package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.Log;
import lk.ijse.agrosmart_systembackend.entity.StaffLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffLogRepository extends JpaRepository<StaffLog, String> {
    List<StaffLog> findByLog(Log log);
}
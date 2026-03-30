package lk.ijse.agrosmart_systembackend.repository;

import lk.ijse.agrosmart_systembackend.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {
    @Query("SELECT MAX(l.logId) FROM Log l")
    String findMaxLogId();
}
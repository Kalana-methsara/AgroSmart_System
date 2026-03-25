package lk.ijse.agrosmart_systembackend.service;


import lk.ijse.agrosmart_systembackend.dto.LogDTO;

import java.util.List;

public interface LogService {
    String saveLog(LogDTO logDTO);
    String updateLog(LogDTO logDTO);
    LogDTO searchLog(String id);
    String deleteLog(String id);
    List<LogDTO> getAllLogs();
}

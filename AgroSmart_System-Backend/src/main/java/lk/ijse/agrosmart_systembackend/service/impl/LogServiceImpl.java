package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.LogDTO;
import lk.ijse.agrosmart_systembackend.entity.Crop;
import lk.ijse.agrosmart_systembackend.entity.Field;
import lk.ijse.agrosmart_systembackend.entity.Log;
import lk.ijse.agrosmart_systembackend.repository.CropRepository;
import lk.ijse.agrosmart_systembackend.repository.FieldRepository;
import lk.ijse.agrosmart_systembackend.repository.LogRepository;
import lk.ijse.agrosmart_systembackend.service.LogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final FieldRepository fieldRepository;
    private final CropRepository cropRepository;
    private final ModelMapper modelMapper;

    @Override
    public String saveLog(LogDTO logDTO) {

        String newId = generateLogID();

        if (logRepository.existsById(newId)) {
            throw new RuntimeException("Generated Log ID already exists: " + newId);
        }

        // Fetch Field entity
        Field field = fieldRepository.findById(logDTO.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found with ID: " + logDTO.getFieldId()));

        // Fetch Crop entity
        Crop crop = cropRepository.findById(logDTO.getCropId())
                .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + logDTO.getCropId()));

        Log log = Log.builder()
                .logId(newId)
                .date(logDTO.getDate() != null ? logDTO.getDate() : LocalDate.now())
                .details(logDTO.getDetails())
                .temperature(logDTO.getTemperature())
                .observedImg(logDTO.getObservedImg())
                .field(field)
                .crop(crop)
                .build();

        logRepository.save(log);
        return "Log saved successfully";
    }

    @Override
    public String updateLog(LogDTO logDTO) {

        Log existingLog = logRepository.findById(logDTO.getLogId())
                .orElseThrow(() -> new RuntimeException("Log not found with ID: " + logDTO.getLogId()));

        // Fetch Field entity if fieldId is provided
        Field field = null;
        if (logDTO.getFieldId() != null) {
            field = fieldRepository.findById(logDTO.getFieldId())
                    .orElseThrow(() -> new RuntimeException("Field not found with ID: " + logDTO.getFieldId()));
        }

        // Fetch Crop entity if cropId is provided
        Crop crop = null;
        if (logDTO.getCropId() != null) {
            crop = cropRepository.findById(logDTO.getCropId())
                    .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + logDTO.getCropId()));
        }

        Log log = Log.builder()
                .logId(logDTO.getLogId())
                .date(logDTO.getDate() != null ? logDTO.getDate() : LocalDate.now())
                .details(logDTO.getDetails())
                .temperature(logDTO.getTemperature())
                .observedImg(logDTO.getObservedImg())
                .field(field)
                .crop(crop)
                .build();

        logRepository.save(existingLog);
        return "Log updated successfully";
    }

    @Override
    public LogDTO searchLog(String id) {

        Log log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with ID: " + id));

        return modelMapper.map(log, LogDTO.class);
    }

    @Override
    public String deleteLog(String id) {

        if (!logRepository.existsById(id)) {
            throw new RuntimeException("Log not found with ID: " + id);
        }

        logRepository.deleteById(id);
        return "Log deleted successfully";
    }

    @Override
    public List<LogDTO> getAllLogs() {

        return modelMapper.map(
                logRepository.findAll(),
                new TypeToken<List<LogDTO>>() {}.getType()
        );
    }

    private String generateLogID() {

        List<Log> logList = logRepository.findAll();

        if (logList.isEmpty()) {
            return "L001";
        }

        String lastId = logList.get(logList.size() - 1).getLogId();
        int newId = Integer.parseInt(lastId.substring(1)) + 1;

        return String.format("L%03d", newId);
    }
}
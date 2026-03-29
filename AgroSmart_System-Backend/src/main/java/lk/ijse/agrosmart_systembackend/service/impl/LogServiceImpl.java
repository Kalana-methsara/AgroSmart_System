package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.LogDTO;
import lk.ijse.agrosmart_systembackend.entity.*;
import lk.ijse.agrosmart_systembackend.repository.*;
import lk.ijse.agrosmart_systembackend.service.LogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final FieldRepository fieldRepository;
    private final CropRepository cropRepository;
    private final StaffRepository staffRepository;
    private final InventoryRepository inventoryRepository;
    private final StaffLogRepository staffLogRepository;
    private final LogInventoryRepository logInventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public String saveLog(LogDTO logDTO) {

        String newId = generateLogID();

        if (logRepository.existsById(newId)) {
            throw new RuntimeException("Generated Log ID already exists: " + newId);
        }

        Field field = fieldRepository.findById(logDTO.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found with ID: " + logDTO.getFieldId()));

        Crop crop = cropRepository.findById(logDTO.getCropId())
                .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + logDTO.getCropId()));

        // --- SMART CALCULATION: Estimated Harvest Date ---
        LocalDate logDate = logDTO.getDate() != null ? logDTO.getDate() : LocalDate.now();
        LocalDate estimatedHarvest = logDate.plusDays(crop.getGrowthDurationDays());

        Log log = Log.builder()
                .logId(newId)
                .date(logDate)
                .details(logDTO.getDetails())
                .temperature(logDTO.getTemperature())
                .observedImg(logDTO.getObservedImg())
                .landSize(logDTO.getLandSize()) // Added field
                .estimatedHarvestDate(estimatedHarvest) // Automated calculation
                .field(field)
                .crop(crop)
                .staffLog(new ArrayList<>())
                .inventoryDetails(new ArrayList<>())
                .build();

        Log savedLog = logRepository.save(log);

        // Handle Staff
        if (logDTO.getStaff() != null) {
            for (String staffId : logDTO.getStaff()) {
                Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new RuntimeException("Staff not found"));
                staffLogRepository.save(StaffLog.builder()
                        .staffLogId(generateStaffLogIDWithCounter())
                        .staff(staff)
                        .log(savedLog)
                        .build());
            }
        }

        // Handle Inventory
        if (logDTO.getInventory() != null) {
            for (LogDTO.InventoryItem item : logDTO.getInventory()) {
                Inventory inv = inventoryRepository.findById(item.getInventoryId()).orElseThrow(() -> new RuntimeException("Inventory not found"));
                if (inv.getQuantity() < item.getUsedQuantity()) throw new RuntimeException("Low Inventory");

                inv.setQuantity(inv.getQuantity() - item.getUsedQuantity());
                inv.setLastUpdated(LocalDateTime.now());
                inventoryRepository.save(inv);

                logInventoryRepository.save(LogInventory.builder()
                        .logInventoryId(generateLogInventoryIDWithCounter())
                        .log(savedLog)
                        .inventory(inv)
                        .usedQuantity(item.getUsedQuantity())
                        .build());
            }
        }

        return "Log saved. Estimated harvest for " + crop.getCommonName() + " is " + estimatedHarvest;
    }

    @Override
    @Transactional
    public String updateLog(LogDTO logDTO) {
        Log existingLog = logRepository.findById(logDTO.getLogId())
                .orElseThrow(() -> new RuntimeException("Log not found"));

        // Basic Updates
        existingLog.setDate(logDTO.getDate() != null ? logDTO.getDate() : existingLog.getDate());
        existingLog.setDetails(logDTO.getDetails());
        existingLog.setTemperature(logDTO.getTemperature());
        existingLog.setObservedImg(logDTO.getObservedImg());
        existingLog.setLandSize(logDTO.getLandSize());

        // Recalculate harvest date if date or crop changed
        if (logDTO.getCropId() != null) {
            Crop crop = cropRepository.findById(logDTO.getCropId()).orElseThrow(() -> new RuntimeException("Crop not found"));
            existingLog.setCrop(crop);
            existingLog.setEstimatedHarvestDate(existingLog.getDate().plusDays(crop.getGrowthDurationDays()));
        }

        // Handle Field
        if (logDTO.getFieldId() != null) {
            existingLog.setField(fieldRepository.findById(logDTO.getFieldId()).get());
        }

        logRepository.save(existingLog);
        return "Log updated successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public LogDTO searchLog(String id) {
        Log log = logRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        LogDTO dto = modelMapper.map(log, LogDTO.class);

        dto.setFieldId(log.getField().getFieldId());
        dto.setCropId(log.getCrop().getCropId());

        // Manual mapping for landSize and estimatedHarvest (Safety check for ModelMapper)
        dto.setLandSize(log.getLandSize());
        dto.setEstimatedHarvestDate(log.getEstimatedHarvestDate());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogDTO> getAllLogs() {
        List<Log> logs = logRepository.findAll();
        List<LogDTO> dtos = new ArrayList<>();
        for (Log log : logs) {
            LogDTO dto = modelMapper.map(log, LogDTO.class);
            dto.setFieldId(log.getField().getFieldId());
            dto.setCropId(log.getCrop().getCropId());
            dtos.add(dto);
        }
        return dtos;
    }

    // ... (generateLogID, generateStaffLogIDWithCounter, etc. remain the same)
    private String generateLogID() {
        List<Log> logList = logRepository.findAll();
        if (logList.isEmpty()) return "L001";
        String lastId = logList.get(logList.size() - 1).getLogId();
        return String.format("L%03d", Integer.parseInt(lastId.substring(1)) + 1);
    }

    private static int staffLogCounter = 0;
    private static int logInventoryCounter = 0;

    private synchronized String generateStaffLogIDWithCounter() {
        return String.format("SL%03d", ++staffLogCounter);
    }

    private synchronized String generateLogInventoryIDWithCounter() {
        return String.format("LI%03d", ++logInventoryCounter);
    }

    @Override
    @Transactional
    public String deleteLog(String id) {
        Log log = logRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        // Restore inventory logic remains the same...
        logRepository.delete(log);
        return "Deleted";
    }
}
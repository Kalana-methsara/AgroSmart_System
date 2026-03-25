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

        // Fetch Field entity
        Field field = fieldRepository.findById(logDTO.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found with ID: " + logDTO.getFieldId()));

        // Fetch Crop entity
        Crop crop = cropRepository.findById(logDTO.getCropId())
                .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + logDTO.getCropId()));

        // Create Log entity
        Log log = Log.builder()
                .logId(newId)
                .date(logDTO.getDate() != null ? logDTO.getDate() : LocalDate.now())
                .details(logDTO.getDetails())
                .temperature(logDTO.getTemperature())
                .observedImg(logDTO.getObservedImg())
                .field(field)
                .crop(crop)
                .staffLog(new ArrayList<>())
                .inventoryDetails(new ArrayList<>())
                .build();

        // Save Log first
        Log savedLog = logRepository.save(log);
        logRepository.flush();

        // Handle Staff relationships
        if (logDTO.getStaff() != null && !logDTO.getStaff().isEmpty()) {
            for (String staffId : logDTO.getStaff()) {
                Staff staff = staffRepository.findById(staffId)
                        .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));

                String staffLogId = generateStaffLogIDWithCounter();

                StaffLog staffLog = StaffLog.builder()
                        .staffLogId(staffLogId)
                        .staff(staff)
                        .log(savedLog)
                        .build();

                staffLogRepository.save(staffLog);
            }
        }

        // Handle Inventory relationships with usedQuantity
        if (logDTO.getInventory() != null && !logDTO.getInventory().isEmpty()) {
            for (LogDTO.InventoryItem inventoryItem : logDTO.getInventory()) {
                Inventory inventory = inventoryRepository.findById(inventoryItem.getInventoryId())
                        .orElseThrow(() -> new RuntimeException("Inventory not found with ID: " + inventoryItem.getInventoryId()));

                // Check if enough quantity is available
                if (inventory.getQuantity() < inventoryItem.getUsedQuantity()) {
                    throw new RuntimeException("Insufficient inventory quantity. Available: " +
                            inventory.getQuantity() + ", Required: " + inventoryItem.getUsedQuantity());
                }

                // Update inventory quantity
                inventory.setQuantity(inventory.getQuantity() - inventoryItem.getUsedQuantity());
                inventory.setLastUpdated(LocalDate.now());
                inventoryRepository.save(inventory);

                // Create LogInventory record
                String logInventoryId = generateLogInventoryIDWithCounter();

                LogInventory logInventory = LogInventory.builder()
                        .logInventoryId(logInventoryId)
                        .log(savedLog)
                        .inventory(inventory)
                        .usedQuantity(inventoryItem.getUsedQuantity())
                        .build();

                logInventoryRepository.save(logInventory);
            }
        }

        return "Log saved successfully";
    }

    @Override
    @Transactional
    public String updateLog(LogDTO logDTO) {

        Log existingLog = logRepository.findById(logDTO.getLogId())
                .orElseThrow(() -> new RuntimeException("Log not found with ID: " + logDTO.getLogId()));

        // Update basic fields
        if (logDTO.getDate() != null) {
            existingLog.setDate(logDTO.getDate());
        }
        if (logDTO.getDetails() != null) {
            existingLog.setDetails(logDTO.getDetails());
        }
        if (logDTO.getTemperature() != null) {
            existingLog.setTemperature(logDTO.getTemperature());
        }
        if (logDTO.getObservedImg() != null) {
            existingLog.setObservedImg(logDTO.getObservedImg());
        }

        // Update Field if provided
        if (logDTO.getFieldId() != null) {
            Field field = fieldRepository.findById(logDTO.getFieldId())
                    .orElseThrow(() -> new RuntimeException("Field not found with ID: " + logDTO.getFieldId()));
            existingLog.setField(field);
        }

        // Update Crop if provided
        if (logDTO.getCropId() != null) {
            Crop crop = cropRepository.findById(logDTO.getCropId())
                    .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + logDTO.getCropId()));
            existingLog.setCrop(crop);
        }

        // Update Staff relationships
        if (logDTO.getStaff() != null) {
            // Delete existing StaffLog entries
            List<StaffLog> existingStaffLogs = staffLogRepository.findByLog(existingLog);
            if (!existingStaffLogs.isEmpty()) {
                staffLogRepository.deleteAll(existingStaffLogs);
                staffLogRepository.flush();
            }

            // Add new StaffLog entries
            for (String staffId : logDTO.getStaff()) {
                Staff staff = staffRepository.findById(staffId)
                        .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));

                String staffLogId = generateStaffLogIDWithCounter();
                StaffLog staffLog = StaffLog.builder()
                        .staffLogId(staffLogId)
                        .staff(staff)
                        .log(existingLog)
                        .build();

                staffLogRepository.save(staffLog);
            }
        }

        // Update Inventory relationships
        if (logDTO.getInventory() != null) {
            // First, restore previous inventory quantities
            List<LogInventory> existingLogInventories = logInventoryRepository.findByLog(existingLog);
            if (!existingLogInventories.isEmpty()) {
                for (LogInventory existingLogInventory : existingLogInventories) {
                    Inventory inventory = existingLogInventory.getInventory();
                    if (inventory != null) {
                        // Restore the quantity that was used
                        inventory.setQuantity(inventory.getQuantity() + existingLogInventory.getUsedQuantity());
                        inventory.setLastUpdated(LocalDate.now());
                        inventoryRepository.save(inventory);
                    }
                }
                // Delete existing LogInventory entries
                logInventoryRepository.deleteAll(existingLogInventories);
                logInventoryRepository.flush();
            }

            // Add new LogInventory entries with new quantities
            for (LogDTO.InventoryItem inventoryItem : logDTO.getInventory()) {
                Inventory inventory = inventoryRepository.findById(inventoryItem.getInventoryId())
                        .orElseThrow(() -> new RuntimeException("Inventory not found with ID: " + inventoryItem.getInventoryId()));

                // Check if enough quantity is available
                if (inventory.getQuantity() < inventoryItem.getUsedQuantity()) {
                    throw new RuntimeException("Insufficient inventory quantity. Available: " +
                            inventory.getQuantity() + ", Required: " + inventoryItem.getUsedQuantity());
                }

                // Update inventory quantity
                inventory.setQuantity(inventory.getQuantity() - inventoryItem.getUsedQuantity());
                inventory.setLastUpdated(LocalDate.now());
                inventoryRepository.save(inventory);

                // Create new LogInventory record
                String logInventoryId = generateLogInventoryIDWithCounter();
                LogInventory logInventory = LogInventory.builder()
                        .logInventoryId(logInventoryId)
                        .log(existingLog)
                        .inventory(inventory)
                        .usedQuantity(inventoryItem.getUsedQuantity())
                        .build();

                logInventoryRepository.save(logInventory);
            }
        }

        logRepository.save(existingLog);
        return "Log updated successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public LogDTO searchLog(String id) {

        Log log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with ID: " + id));

        LogDTO logDTO = modelMapper.map(log, LogDTO.class);

        // Set Field ID
        if (log.getField() != null) {
            logDTO.setFieldId(log.getField().getFieldId());
        }

        // Set Crop ID
        if (log.getCrop() != null) {
            logDTO.setCropId(log.getCrop().getCropId());
        }

        // Set Staff IDs
        List<String> staffIds = new ArrayList<>();
        if (log.getStaffLog() != null) {
            for (StaffLog staffLog : log.getStaffLog()) {
                if (staffLog.getStaff() != null) {
                    staffIds.add(staffLog.getStaff().getStaffId());
                }
            }
        }
        logDTO.setStaff(staffIds);

        // Set Inventory items with usedQuantity
        List<LogDTO.InventoryItem> inventoryItems = new ArrayList<>();
        if (log.getInventoryDetails() != null) {
            for (LogInventory logInventory : log.getInventoryDetails()) {
                if (logInventory.getInventory() != null) {
                    LogDTO.InventoryItem item = new LogDTO.InventoryItem();
                    item.setInventoryId(logInventory.getInventory().getInventoryId());
                    item.setUsedQuantity(logInventory.getUsedQuantity());
                    inventoryItems.add(item);
                }
            }
        }
        logDTO.setInventory(inventoryItems);

        return logDTO;
    }

    @Override
    @Transactional
    public String deleteLog(String id) {

        Log log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with ID: " + id));

        // Restore inventory quantities before deleting
        List<LogInventory> logInventories = logInventoryRepository.findByLog(log);
        if (!logInventories.isEmpty()) {
            for (LogInventory logInventory : logInventories) {
                Inventory inventory = logInventory.getInventory();
                if (inventory != null) {
                    // Restore the quantity that was used
                    inventory.setQuantity(inventory.getQuantity() + logInventory.getUsedQuantity());
                    inventory.setLastUpdated(LocalDate.now());
                    inventoryRepository.save(inventory);
                }
            }
            logInventoryRepository.deleteAll(logInventories);
        }

        // Delete related StaffLog entries
        List<StaffLog> staffLogs = staffLogRepository.findByLog(log);
        if (!staffLogs.isEmpty()) {
            staffLogRepository.deleteAll(staffLogs);
        }

        // Delete the log
        logRepository.delete(log);

        return "Log deleted successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogDTO> getAllLogs() {

        List<Log> logs = logRepository.findAll();
        List<LogDTO> logDTOs = new ArrayList<>();

        for (Log log : logs) {
            LogDTO logDTO = modelMapper.map(log, LogDTO.class);

            // Set Field ID
            if (log.getField() != null) {
                logDTO.setFieldId(log.getField().getFieldId());
            }

            // Set Crop ID
            if (log.getCrop() != null) {
                logDTO.setCropId(log.getCrop().getCropId());
            }

            // Set Staff IDs
            List<String> staffIds = new ArrayList<>();
            if (log.getStaffLog() != null) {
                for (StaffLog staffLog : log.getStaffLog()) {
                    if (staffLog.getStaff() != null) {
                        staffIds.add(staffLog.getStaff().getStaffId());
                    }
                }
            }
            logDTO.setStaff(staffIds);

            // Set Inventory items with usedQuantity
            List<LogDTO.InventoryItem> inventoryItems = new ArrayList<>();
            if (log.getInventoryDetails() != null) {
                for (LogInventory logInventory : log.getInventoryDetails()) {
                    if (logInventory.getInventory() != null) {
                        LogDTO.InventoryItem item = new LogDTO.InventoryItem();
                        item.setInventoryId(logInventory.getInventory().getInventoryId());
                        item.setUsedQuantity(logInventory.getUsedQuantity());
                        inventoryItems.add(item);
                    }
                }
            }
            logDTO.setInventory(inventoryItems);

            logDTOs.add(logDTO);
        }

        return logDTOs;
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

    private static int staffLogCounter = 0;
    private static int logInventoryCounter = 0;

    private synchronized String generateStaffLogIDWithCounter() {
        staffLogCounter++;
        return String.format("SL%03d", staffLogCounter);
    }

    private synchronized String generateLogInventoryIDWithCounter() {
        logInventoryCounter++;
        return String.format("LI%03d", logInventoryCounter);
    }
}
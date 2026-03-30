package lk.ijse.agrosmart_systembackend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.agrosmart_systembackend.dto.FieldRequestDTO;
import lk.ijse.agrosmart_systembackend.entity.*;
import lk.ijse.agrosmart_systembackend.repository.*;
import lk.ijse.agrosmart_systembackend.service.FieldRequestService;
import lk.ijse.agrosmart_systembackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FieldRequestServiceImpl implements FieldRequestService {

    private final FieldRepository fieldRepository;
    private final CropRepository cropRepository;
    private final StaffRepository staffRepository;
    private final LogRepository logRepository;
    private final RestTemplate restTemplate;
    private final TaskService taskService;

    @Value("${openweather.api.key}")
    private String weatherApiKey;

    @Override
    public String saveField(FieldRequestDTO dto) {
        Field field = fieldRepository.findById(dto.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found"));

        Log log = new Log();
        log.setLogId(generateLogID());
        log.setDate(LocalDate.now());
        log.setDetails(dto.getDetails());
        log.setField(field);

        setTemperature(log, field);

        List<FieldCrop> crops = dto.getCrops().stream().map(c -> {
            Crop crop = cropRepository.findById(c.getName())
                    .orElseThrow(() -> new RuntimeException("Crop not found: " + c.getName()));

            FieldCrop fc = new FieldCrop();
            fc.setFieldCropId("FC-" + UUID.randomUUID());
            fc.setAssignedDate(LocalDate.now());
            fc.setCrop(crop);
            fc.setField(field);
            fc.setLog(log);
            fc.setArea(Double.parseDouble(c.getArea()));
            return fc;
        }).toList();

        List<FieldStaff> staffs = dto.getStaffIds().stream().map(id -> {
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Staff not found: " + id));

            FieldStaff fs = new FieldStaff();
            fs.setFieldStaffId("FS-" + UUID.randomUUID());
            fs.setAssignedDate(LocalDate.now());
            fs.setStaff(staff);
            fs.setField(field);
            fs.setLog(log);
            return fs;
        }).toList();

        log.setFieldCrops(crops);
        log.setFieldStaffs(staffs);
        logRepository.save(log);
        crops.forEach(fc -> taskService.generateTasksForFieldCrop(fc, fc.getArea()));

        return "Saved successfully";
    }

    private void setTemperature(Log log, Field field) {
        try {
            String[] parts = field.getCoordinates().split(",");
            double lat = Double.parseDouble(parts[0].trim());
            double lon = Double.parseDouble(parts[1].trim());

            String url = "https://api.openweathermap.org/data/2.5/weather?lat="
                    + lat + "&lon=" + lon
                    + "&appid=" + weatherApiKey + "&units=metric";

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.get("main") instanceof Map<?, ?> main) {
                log.setTemperature(String.valueOf(main.get("temp")));
            } else {
                log.setTemperature("N/A");
            }
        } catch (Exception e) {
            log.setTemperature("N/A");
        }
    }

    private String generateLogID() {
        String maxId = logRepository.findMaxLogId();
        if (maxId == null) return "L001";
        return String.format("L%03d", Integer.parseInt(maxId.substring(1)) + 1);
    }
}
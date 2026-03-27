package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.CropDTO;
import lk.ijse.agrosmart_systembackend.entity.Crop;
import lk.ijse.agrosmart_systembackend.repository.CropRepository;
import lk.ijse.agrosmart_systembackend.service.CropService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropRepository cropRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    @Override
    public String saveCrop(CropDTO cropDTO) {

        String newId = generateCropID();

        if (cropRepository.existsById(newId)) {
            throw new RuntimeException("Generated Crop ID already exists: " + newId);
        }

        if (cropRepository.existsByCommonName(cropDTO.getCommonName())) {
            throw new RuntimeException("Crop common name already exists");
        }

        if (cropRepository.existsByScientificName(cropDTO.getScientificName())) {
            throw new RuntimeException("Scientific name already exists");
        }

        Crop crop = Crop.builder()
                .cropId(newId)
                .commonName(cropDTO.getCommonName())
                .scientificName(cropDTO.getScientificName())
                .category(cropDTO.getCategory())
                .season(cropDTO.getSeason())
                .cropImg(cropDTO.getCropImg())
                .growthDurationDays(cropDTO.getGrowthDurationDays())
                .fertilizerStartDay(cropDTO.getFertilizerStartDay())
                .fertilizerIntervalDays(cropDTO.getFertilizerIntervalDays())
                .waterRequirementIndex(cropDTO.getWaterRequirementIndex())
                .minTemp(cropDTO.getMinTemp())
                .maxTemp(cropDTO.getMaxTemp())
                .expectedYieldPerUnit(cropDTO.getExpectedYieldPerUnit())
                .currentMarketPrice(cropDTO.getCurrentMarketPrice())
                .build();

        cropRepository.save(crop);
        return "Crop saved successfully";
    }

    @Override
    public String updateCrop(CropDTO cropDTO) {

        if (!cropRepository.existsById(cropDTO.getCropId())) {
            throw new RuntimeException("Crop not found");
        }

        Crop existing = cropRepository.findById(cropDTO.getCropId()).get();

        if (!existing.getCommonName().equals(cropDTO.getCommonName()) &&
                cropRepository.existsByCommonName(cropDTO.getCommonName())) {
            throw new RuntimeException("Crop common name already exists");
        }

        if (!existing.getScientificName().equals(cropDTO.getScientificName()) &&
                cropRepository.existsByScientificName(cropDTO.getScientificName())) {
            throw new RuntimeException("Scientific name already exists");
        }

        Crop crop = Crop.builder()
                .cropId(cropDTO.getCropId())
                .commonName(cropDTO.getCommonName())
                .scientificName(cropDTO.getScientificName())
                .category(cropDTO.getCategory())
                .season(cropDTO.getSeason())
                .cropImg(cropDTO.getCropImg())
                .growthDurationDays(cropDTO.getGrowthDurationDays())
                .fertilizerStartDay(cropDTO.getFertilizerStartDay())
                .fertilizerIntervalDays(cropDTO.getFertilizerIntervalDays())
                .waterRequirementIndex(cropDTO.getWaterRequirementIndex())
                .minTemp(cropDTO.getMinTemp())
                .maxTemp(cropDTO.getMaxTemp())
                .expectedYieldPerUnit(cropDTO.getExpectedYieldPerUnit())
                .currentMarketPrice(cropDTO.getCurrentMarketPrice())
                .build();

        cropRepository.save(crop);
        return "Crop updated successfully";
    }

    @Override
    public String deleteCrop(String id) {

        if (!cropRepository.existsById(id)) {
            throw new RuntimeException("Crop not found");
        }

        cropRepository.deleteById(id);
        return "Crop deleted successfully";
    }

    @Override
    public CropDTO getCrop(String id) {

        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop not found"));

        return modelMapper.map(crop, CropDTO.class);
    }

    @Override
    public List<CropDTO> getAllCrops() {

        return modelMapper.map(
                cropRepository.findAll(),
                new TypeToken<List<CropDTO>>() {}.getType()
        );
    }

    @Override
    public double predictYield(String cropId, double landSize) {
        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new RuntimeException("Crop not found"));

        //  අක්කරයකට අස්වැන්න * වගා කරන බිම් ප්‍රමාණය
        return crop.getExpectedYieldPerUnit() * landSize;
    }

    @Override
    public List<String> getUpcomingTasks(String cropId, String startDate) {
        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + cropId));

        List<String> taskSchedule = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate); // String එක LocalDate එකකට හැරවීම

        // 1. පළමු පොහොර යෙදිය යුතු දිනය ගණනය කිරීම
        LocalDate firstFertilizerDate = start.plusDays(crop.getFertilizerStartDay());
        taskSchedule.add("First Fertilizing Date: " + firstFertilizerDate);

        // 2. ඊළඟ වාරයන් කිහිපයක් ගණනය කිරීම (උදාහරණයක් ලෙස වාර 3ක්)
        LocalDate nextDate = firstFertilizerDate;
        for (int i = 1; i <= 3; i++) {
            nextDate = nextDate.plusDays(crop.getFertilizerIntervalDays());
            taskSchedule.add("Upcoming Fertilizing Session " + i + ": " + nextDate);
        }

        // 3. අස්වැන්න නෙළන දළ දිනය ගණනය කිරීම
        LocalDate harvestDate = start.plusDays(crop.getGrowthDurationDays());
        taskSchedule.add("Estimated Harvest Date: " + harvestDate);

        return taskSchedule;
    }
    @Override
    public String generateWeatherAlert(String cropId, String location) {
        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + cropId));

        // It's better to move these to application.properties later
        String apiKey = "36ae79b3c48ce2a5e18f45a589b503cd";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey + "&units=metric";

        try {
            // Fetching data from the external API
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null) {
                Map<String, Object> main = (Map<String, Object>) response.get("main");
                double humidity = Double.parseDouble(main.get("humidity").toString());

                // වැසි ලැබීමේ සම්භාවිතාව වැඩි නම් (Humidity > 80%)
                // සහ එම භෝගයට ජලය අඩුවෙන් අවශ්‍ය නම් (Water Index < 5)
                if (humidity > 80 && crop.getWaterRequirementIndex() < 5) {
                    return "Alert: Heavy rain expected in " + location + ". " +
                            "Please avoid irrigation or fertilizing your " + crop.getCommonName() +
                            " crop today to prevent resource wastage.";
                }
            }
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error fetching weather data: " + e.getMessage());
            return "Weather data currently unavailable for " + location + ". Please try again later.";
        }

        return "Current weather is optimal for " + crop.getCommonName() + " cultivation in " + location + ".";
    }

    private String generateCropID() {

        List<Crop> cropList = cropRepository.findAll();

        if (cropList.isEmpty()) {
            return "C001";
        }

        String lastId = cropList.get(cropList.size() - 1).getCropId();
        int newId = Integer.parseInt(lastId.substring(1)) + 1;

        return String.format("C%03d", newId);
    }
}

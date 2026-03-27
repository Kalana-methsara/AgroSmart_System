package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.CropDTO;
import lk.ijse.agrosmart_systembackend.service.CropService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/crop")
@RequiredArgsConstructor
public class CropController {

    private final CropService cropService;

    //  Save Crop
    @PostMapping
    public ResponseEntity<ApiResponse> saveCrop(@RequestBody CropDTO cropDTO) {
        return new ResponseEntity<>(
                new ApiResponse(201, "Crop saved successfully", cropService.saveCrop(cropDTO)),
                HttpStatus.CREATED
        );
    }

    //  Update Crop
    @PutMapping
    public ResponseEntity<ApiResponse> updateCrop(@RequestBody CropDTO cropDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Crop updated successfully", cropService.updateCrop(cropDTO)),
                HttpStatus.OK
        );
    }

    //  Delete Crop
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCrop(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Crop deleted successfully", cropService.deleteCrop(id)),
                HttpStatus.OK
        );
    }

    //  Get Single Crop
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCrop(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Crop fetched successfully", cropService.getCrop(id)),
                HttpStatus.OK
        );
    }

    //  Get All Crops
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCrops() {
        List<CropDTO> list = cropService.getAllCrops();
        return new ResponseEntity<>(
                new ApiResponse(200, "All crops fetched successfully", list),
                HttpStatus.OK
        );
    }
    // Predict Yield Endpoint
    @GetMapping("/{cropId}/predict-yield")
    public ResponseEntity<ApiResponse> predictYield(
            @PathVariable String cropId,
            @RequestParam double landSize) {

        double yield = cropService.predictYield(cropId, landSize);
        return ResponseEntity.ok(new ApiResponse(200, "Yield prediction calculated", yield + " kg"));
    }

    // Upcoming Tasks Endpoint
    @GetMapping("/{cropId}/tasks")
    public ResponseEntity<ApiResponse> getTasks(
            @PathVariable String cropId,
            @RequestParam String startDate) { // Use format YYYY-MM-DD

        List<String> tasks = cropService.getUpcomingTasks(cropId, startDate);
        return ResponseEntity.ok(new ApiResponse(200, "Task schedule generated", tasks));
    }

    // Weather Alert Endpoint
    @GetMapping("/{cropId}/weather-alert")
    public ResponseEntity<ApiResponse> getWeatherAlert(
            @PathVariable String cropId,
            @RequestParam String location) {

        String alertMessage = cropService.generateWeatherAlert(cropId, location);

        return new ResponseEntity<>(
                new ApiResponse(200, "Weather alert generated successfully", alertMessage),
                HttpStatus.OK
        );
    }
}
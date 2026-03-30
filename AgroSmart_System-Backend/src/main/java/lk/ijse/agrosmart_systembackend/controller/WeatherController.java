package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.FieldRequestDTO;
import lk.ijse.agrosmart_systembackend.service.FieldRequestService;
import lk.ijse.agrosmart_systembackend.service.WeatherService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<ApiResponse> getWeatherStats(){
        return new ResponseEntity<>(
                new ApiResponse(200, "Current Weather Details", weatherService.getCurrentWeather()),
                HttpStatus.OK
        );
    }
}

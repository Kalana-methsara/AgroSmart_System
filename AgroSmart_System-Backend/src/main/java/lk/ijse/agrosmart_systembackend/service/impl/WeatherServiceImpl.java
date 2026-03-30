package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.WeatherDTO;
import lk.ijse.agrosmart_systembackend.service.WeatherService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final String API_KEY = "36ae79b3c48ce2a5e18f45a589b503cd&units";
    private final String CITY = "Anuradhapura";
    private final String URL = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&appid=" + API_KEY + "&units=metric";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public WeatherDTO getCurrentWeather() {
        // බාහිර API එකට Call එකක් දී දත්ත ලබා ගැනීම
        Map<String, Object> response = restTemplate.getForObject(URL, Map.class);

        WeatherDTO dto = new WeatherDTO();
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        List<Map<String, Object>> weather = (List<Map<String, Object>>) response.get("weather");

        dto.setTemp((Double) main.get("temp"));
        dto.setHumidity((Integer) main.get("humidity"));
        dto.setCondition((String) weather.get(0).get("main")); // Rain, Clouds, Clear ආදිය

        // නිර්දේශය (Recommendation) සකස් කිරීම
        if (dto.getCondition().equalsIgnoreCase("Rain")) {
            dto.setRecommendation("අද දින වැසි සහිතයි. වතුර දැමීම (Irrigation) අවශ්‍ය නොවේ.");
        } else if (dto.getTemp() > 30) {
            dto.setRecommendation("අධික රේෂ්ණය පවතියි. වගාවන්ට වැඩිපුර වතුර සැපයීමට සලකා බලන්න.");
        } else {
            dto.setRecommendation("කාලගුණය සාමාන්‍යයි. නියමිත පරිදි වැඩ කටයුතු කරගෙන යන්න.");
        }

        return dto;
    }
}

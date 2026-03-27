package lk.ijse.agrosmart_systembackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // JSON එකේ තියෙන අපිට අනවශ්‍ය fields ignore කිරීමට
public class WeatherResponse {

    private Main main;
    private List<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private Rain rain;
    private String name; // නගරයේ නම

    @Data
    public static class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;
    }

    @Data
    public static class Weather {
        private int id;
        private String main;        // උදා: "Rain", "Clouds", "Clear"
        private String description; // උදා: "light rain"
        private String icon;
    }

    @Data
    public static class Clouds {
        private int all; // වලාකුළු ප්‍රමාණය (%)
    }

    @Data
    public static class Wind {
        private double speed;
        private int deg;
    }

    @Data
    public static class Rain {
        @com.fasterxml.jackson.annotation.JsonProperty("1h")
        private double oneHour; // පසුගිය පැය ඇතුළත වර්ෂාපතනය (mm)
    }
}
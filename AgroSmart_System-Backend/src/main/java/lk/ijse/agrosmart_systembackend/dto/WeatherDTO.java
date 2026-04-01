package lk.ijse.agrosmart_systembackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO implements Serializable {
    private double temp;
    private int humidity;
    private String condition;
    private String recommendation;
}
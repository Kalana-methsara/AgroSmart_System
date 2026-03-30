package lk.ijse.agrosmart_systembackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class FieldRequestDTO {
    private String fieldId;
    private String details;
    private String totalLand;
    private String timestamp;

    private List<String> staffIds;
    private List<CropsDTO> crops;
    @Data
    public static class CropsDTO {
        private String name;
        private String area;
    }
}
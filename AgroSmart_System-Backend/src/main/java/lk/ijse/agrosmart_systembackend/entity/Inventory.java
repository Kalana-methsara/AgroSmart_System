package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory implements Serializable {
    @Id
    private String inventoryId;
    private String itemName; // උදා: Urea Fertilizer, Chili Seeds
    private String category; // SEED, FERTILIZER, PESTICIDE
    private Double quantity; // දැනට ගබඩාවේ ඇති ප්‍රමාණය
    private String unit;     // kg, L, Packets
//    private String supplierDetails;
    private LocalDate lastUpdated;
}

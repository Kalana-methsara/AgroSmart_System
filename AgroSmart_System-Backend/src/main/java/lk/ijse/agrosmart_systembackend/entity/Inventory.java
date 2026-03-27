package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ijse.agrosmart_systembackend.entity.enums.Category;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory implements Serializable {
    @Id
    private String inventoryId;
    @NotBlank
    private String itemName;
    private Category category; // SEED, FERTILIZER, PESTICIDE
    @NotNull
    private Double quantity;
    @NotBlank
    private String unit;     // kg, L, Packets
    // private String supplierDetails;
    private LocalDateTime lastUpdated;
}

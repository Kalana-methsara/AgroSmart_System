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
public class LogInventory implements Serializable {
    @Id
    private String logInventoryId;

    @ManyToOne
    @JoinColumn(name = "log_id")
    private Log log;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    private Double usedQuantity; // එම අවස්ථාවේදී පාවිච්චි කළ ප්‍රමාණය
}

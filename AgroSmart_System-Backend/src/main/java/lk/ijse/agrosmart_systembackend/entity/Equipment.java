package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Equipment implements Serializable {
    @Id
    private String equipmentId;
    private String category;
    private String type;
    private String status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "field_id")
    private Field field;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id")
    private Staff staff;
}

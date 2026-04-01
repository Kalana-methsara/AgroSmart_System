package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lk.ijse.agrosmart_systembackend.entity.enums.SettingType;
import lk.ijse.agrosmart_systembackend.entity.enums.ValueType;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SalarySetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false, length = 100)
    private String name;

    @Column( length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private SettingType type;   // ALLOWANCE, DEDUCTION, TAX

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    @Builder.Default
    private ValueType valueType = ValueType.FIXED;

    @Builder.Default
    private Boolean isActive = true;


}

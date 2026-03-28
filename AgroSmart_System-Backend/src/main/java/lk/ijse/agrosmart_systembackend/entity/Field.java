package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Field implements Serializable {
    @Id
    private String fieldId;
    private String fieldName;
    private String locationName; // To store "Nuwara Eliya"
    private String coordinates;  // Changed to String to store "6.9497,80.7891"
    private Double area;         // Changed to Double for numeric area
    private String areaUnit;     // To store "acres"
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    @Basic(fetch = FetchType.EAGER)
    private String image;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<Equipment> equipment;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<Log> logs;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<FieldCrop> fieldCrops = new ArrayList<>();

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<FieldStaff> fieldStaffs = new ArrayList<>();
}
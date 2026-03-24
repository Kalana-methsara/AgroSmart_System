package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Field implements Serializable {
    @Id
    private String fieldId;
    private String fieldName;
    @ToString.Exclude
    private Point location;
    private String size;
    @Column(columnDefinition = "LONGTEXT")
    private String fieldImg1;
    @Column(columnDefinition = "LONGTEXT")
    private String fieldImg2;
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<Equipment> equipment;
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<Log> logs;
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<FieldCrop> fieldCrops = new ArrayList<>();
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<FieldStaff> fieldStaffs = new ArrayList<>();

}

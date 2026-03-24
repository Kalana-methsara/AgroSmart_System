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
public class Crop implements Serializable {
    @Id
    private String cropId;
    private String commonName;
    private String scientificName;
    private String category;
    private String season;
    @Column(columnDefinition = "LONGTEXT")
    @Basic(fetch = FetchType.EAGER)
    private String cropImg;
    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<Log> logs;
    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<FieldCrop> fieldCrops = new ArrayList<>();
}

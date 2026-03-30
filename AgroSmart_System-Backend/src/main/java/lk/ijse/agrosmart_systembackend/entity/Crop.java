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

    // --- Smart Management Fields ---
    private Integer growthDurationDays;       // වර්ධන කාලය (දින)
    private Integer fertilizerStartDay;       // පළමු පොහොර දිනය
    private Integer fertilizerIntervalDays;   // පොහොර පරතරය (දින)
    private Integer waterRequirementIndex;    // ජල අවශ්‍යතා මට්ටම (1-10)
    private Double minTemp;                   // Decision Support
    private Double maxTemp;
    private Double expectedYieldPerUnit;      // අස්වැන්න
    private Double currentMarketPrice;        // වෙළඳපල මිල

    // --- Relationships ---
    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FieldCrop> fieldCrops = new ArrayList<>();
}
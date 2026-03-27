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

    // වර්ධන කාලසීමාව (දින වලින්)
    private Integer growthDurationDays;

    // පළමු පොහොර යෙදිය යුතු දිනය (සිටුවා දින කීයකින්ද?)
    private Integer fertilizerStartDay;

    // පොහොර යෙදිය යුතු පරතරය (දින වලින්)
    private Integer fertilizerIntervalDays;

    // ජල අවශ්‍යතා මට්ටම (1-10) - මෙය Weather API එක සමඟ සංසන්දනයට වැදගත් වේ
    private Integer waterRequirementIndex;

    // උෂ්ණත්ව පරාසය (Decision Support සඳහා)
    private Double minTemp;
    private Double maxTemp;

    // අස්වැන්න අනාවැකි සඳහා (ප්‍රදේශයකට සාමාන්‍ය අස්වැන්න Kg වලින්)
    private Double expectedYieldPerUnit;

    // වර්තමාන වෙළඳපල මිල (Profitability Tracker සඳහා)
    private Double currentMarketPrice;

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<Log> logs;
    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<FieldCrop> fieldCrops = new ArrayList<>();
}

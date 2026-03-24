package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Fields implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fieldName;
    @ToString.Exclude
    private Point location;
    private String size;
    @Column(columnDefinition = "LONGTEXT")
    private String fieldImg1;
    @Column(columnDefinition = "LONGTEXT")
    private String fieldImg2;

}

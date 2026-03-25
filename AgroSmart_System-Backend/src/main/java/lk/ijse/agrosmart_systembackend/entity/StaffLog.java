package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StaffLog {
    @Id
    private String staffLogId;
    @ManyToOne
    @JoinColumn(name = "staff_id" ,referencedColumnName = "staffId")
    private Staff staff;
    @ManyToOne
    @JoinColumn(name = "log_id", referencedColumnName = "logId")
    private Log log;
}

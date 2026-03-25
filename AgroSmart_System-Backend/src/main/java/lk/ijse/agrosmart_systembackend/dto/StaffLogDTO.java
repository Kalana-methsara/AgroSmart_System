package lk.ijse.agrosmart_systembackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffLogDTO implements Serializable {

    private String staffLogId;

    @NotBlank(message = "Staff ID cannot be blank")
    private String staffId;

    @NotBlank(message = "Log ID cannot be blank")
    private String logId;
}
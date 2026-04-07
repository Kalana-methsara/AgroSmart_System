package lk.ijse.agrosmart_systembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private String merchant_id;
    private String return_url;
    private String cancel_url;
    private String notify_url;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String order_id;
    private String items;
    private String currency;
    private double amount;
    private String hash; // Security එක සඳහා
}
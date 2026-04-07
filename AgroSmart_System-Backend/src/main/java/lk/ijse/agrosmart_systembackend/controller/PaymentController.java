package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.PaymentRequestDTO;
import lk.ijse.agrosmart_systembackend.util.PayHereUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin
public class PaymentController {

    @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    @PostMapping("/generate-details")
    public ResponseEntity<PaymentRequestDTO> getPaymentDetails(@RequestBody PaymentRequestDTO request) {
        // Hash එක generate කිරීම
        String hash = PayHereUtil.generateHash(merchantId, request.getOrder_id(), request.getAmount(), "LKR", merchantSecret);

        request.setMerchant_id(merchantId);
        request.setHash(hash);

        return ResponseEntity.ok(request);
    }
}

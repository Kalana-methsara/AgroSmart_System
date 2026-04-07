package lk.ijse.agrosmart_systembackend.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class PayHereUtil {

    public static String generateHash(String merchantId, String orderId, double amount, String currency, String merchantSecret) {
        try {
            // PayHere එකට අනිවාර්යයෙන්ම decimal points 2ක් තියෙන්න ඕනේ (2500.00 වගේ)
            DecimalFormat df = new DecimalFormat("0.00");
            String amountFormatted = df.format(amount);

            // 1. Merchant Secret එක මුලින්ම MD5 කරලා UpperCase කරගන්න
            String secretHash = md5(merchantSecret).toUpperCase();

            // 2. පිළිවෙලට String එක සකසා ගන්න
            String rawString = merchantId + orderId + amountFormatted + currency + secretHash;

            // 3. මුළු String එකම නැවත MD5 කරලා UpperCase කරලා hash එක විදිහට ගන්න
            return md5(rawString).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String md5(String input) throws NoSuchAlgorithmException {
        // StandardCharsets.UTF_8 පාවිච්චි කිරීම වඩාත් නිවැරදියි
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            // හැම byte එකක්ම hex string එකකට හරවා append කිරීම
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
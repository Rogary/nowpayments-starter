package com.gary.nowpayments.callback;

import lombok.extern.slf4j.Slf4j;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
public class SignatureVerifier {
    private final String ipnSecret;

    public SignatureVerifier(String ipnSecret) {
        this.ipnSecret = ipnSecret;
    }

    /**
     * Verify HMAC-SHA512 signature from NOWPayments IPN callback.
     * @param signature  x-nowpayments-sig header value
     * @param rawBody    raw request body string
     * @return true if signature matches
     */
    public boolean verify(String signature, String rawBody) {
        if (signature == null || rawBody == null) {
            return false;
        }
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(
                    ipnSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(keySpec);
            byte[] signatureBytes = hmac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));
            String calculated = bytesToHex(signatureBytes);
            return calculated.equals(signature);
        } catch (Exception e) {
            log.error("Signature verification error", e);
            return false;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}

package sevensmurfs.rehub.util;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class SecurityUtil {

    public static final String HASH_ALGORITHM = "SHA-256";

    private SecurityUtil() {
        throw new IllegalStateException("Security util class.");
    }

    public static String hashInput(@NotNull String input) {
        log.debug("Hashing input string.");
        return bytesToHex(digest(input.getBytes()));
    }

    private static byte[] digest(byte[] input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            log.warn("Invalid hashing algorithm.");
            throw new IllegalArgumentException(ex.getMessage());
        }
        return md.digest(input);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

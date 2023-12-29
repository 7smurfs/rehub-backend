package sevensmurfs.rehub.util;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import sevensmurfs.rehub.model.message.request.UserRequest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

@Slf4j
public class SecurityUtil {

    private static final String HASH_ALGORITHM = "SHA-256";

    private static final String KEY_ALGORITHM = "AES";

    private static final String ENCRYPTION_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final String SECRET_KEY = "THISISVERYSECRETSECRETREHUBKEY69";

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

    private static SecretKey generateKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), KEY_ALGORITHM);
    }

    public static String encryptUsername(@NotNull String username) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, generateKey());
        byte[] encryptedBytes = cipher.doFinal(username.getBytes(StandardCharsets.UTF_8));
        log.debug("Username encrypted");
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptUsername(@NotNull String encryptedUsername) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, generateKey());
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedUsername));
        log.debug("Username decrypted");
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String generateEmployeePassword(UserRequest userRequest) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char randomLetter = (char) (random.nextInt(26) + 'A');
            sb.append(randomLetter);
        }
        return userRequest.getFirstName().toUpperCase().charAt(0) +
                          userRequest.getLastName().toUpperCase().charAt(0) +
                          "_" +
                          userRequest.getDateOfBirth().getYear() +
                          "_" + sb;
    }

}

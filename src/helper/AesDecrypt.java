package helper;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AesDecrypt {

    private static final String key = "1aXf0@qX!J6lpAvT";
    private static final String iv = "DxSAIfT3/P^6~A&D";
    private static final String characterEncoding = "UTF-8";
    private static final String cipherTransformation = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithm = "AES";


    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(AesDecrypt.iv.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(AesDecrypt.key.getBytes("UTF-8"), aesEncryptionAlgorithm);

            Cipher cipher = Cipher.getInstance(cipherTransformation);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            String encryptedStr = Base64.getEncoder().encodeToString(encrypted);

            return encryptedStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(AesDecrypt.iv.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(AesDecrypt.key.getBytes("UTF-8"), aesEncryptionAlgorithm);

            Cipher cipher = Cipher.getInstance(cipherTransformation);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


}

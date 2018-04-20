package com.stdakov.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Optional;

public class AesDecrypt {

    private String key = "1aXf0@qX!J6lpAvT";
    private String iv = "DxSAIfT3/P^6~A&D";
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5PADDING";
    private static final String AES_ENCRYPTION_ALGORITHM = "AES";

    public void setKey(String key) {
        this.key = key;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public Optional<String> encrypt(String value) {
        String encryptedStr = null;

        try {
            IvParameterSpec iv = new IvParameterSpec(this.iv.getBytes(AesDecrypt.CHARACTER_ENCODING));
            SecretKeySpec skeySpec = new SecretKeySpec(this.key.getBytes(AesDecrypt.CHARACTER_ENCODING), AesDecrypt.AES_ENCRYPTION_ALGORITHM);

            Cipher cipher = Cipher.getInstance(AesDecrypt.CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            encryptedStr = Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(encryptedStr);
    }

    public Optional<String> decrypt(String encrypted) {
        String decryptedStr = null;

        try {
            IvParameterSpec iv = new IvParameterSpec(this.iv.getBytes(AesDecrypt.CHARACTER_ENCODING));
            SecretKeySpec skeySpec = new SecretKeySpec(this.key.getBytes(AesDecrypt.CHARACTER_ENCODING), AesDecrypt.AES_ENCRYPTION_ALGORITHM);

            Cipher cipher = Cipher.getInstance(AesDecrypt.CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            decryptedStr = new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(decryptedStr);
    }


}

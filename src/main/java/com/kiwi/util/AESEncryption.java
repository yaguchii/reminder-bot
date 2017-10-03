package com.kiwi.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;

@Component
public class AESEncryption implements Serializable {

    private static final IvParameterSpec IV = new IvParameterSpec(System.getenv("ENCRIPTION_IV").getBytes());
    private static final SecretKeySpec KEY = new SecretKeySpec(System.getenv("ENCRIPTION_KEY").getBytes(), "AES");

    public String encrypto(String text) throws Exception {
        Cipher encrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encrypter.init(Cipher.ENCRYPT_MODE, KEY, IV);

        byte[] crypto = encrypter.doFinal(text.getBytes());
        byte[] str64 = java.util.Base64.getEncoder().encode(crypto);
        return new String(str64);
    }

    public String decrypto(String str64) throws Exception {
        Cipher decrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decrypter.init(Cipher.DECRYPT_MODE, KEY, IV);
        byte[] str = java.util.Base64.getDecoder().decode(str64);
        byte[] text = decrypter.doFinal(str);
        return new String(text);
    }
}

package cn.sudoer.javaproj.bean;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import javax.crypto.Cipher;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KeyPairBean {
    private KeyPair keyPair;

    public KeyPairBean() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public String encrypt(String data) {
        // 使用私钥加密
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
            byte[] result = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error(e.toString());
            return "";
        }
    }

    public String decrypt(String data) {
        // 使用公钥解密
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(result);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error(e.toString());
            return "";
        }
    }
}
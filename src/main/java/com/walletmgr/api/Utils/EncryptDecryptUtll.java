package com.walletmgr.api.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Component;

@Component
public class EncryptDecryptUtll {
    public String ALGORITHM = "PBEWithMD5AndDES";
    public String PASSWORD = "hhackers";

    public String Encrypt(String plain) {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm(ALGORITHM);
        pbeEnc.setPassword(PASSWORD);
        return pbeEnc.encrypt(plain);
    }

    public String encryptSHA256(String str) {

        String sha = "";

        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            sha = sb.toString();

            // System.out.println("sha : " + sha);

        } catch (NoSuchAlgorithmException e) {
            // e.printStackTrace();
            System.out.println("Encrypt Error - NoSuchAlgorithmException");
            sha = null;
        }

        return sha;
    }
}

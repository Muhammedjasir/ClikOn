package com.tids.clikonservice.Utils.RetrofitUtils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class AesBase64Wrapper {

    private static String IV = "T3CH10G!C@MNYTRF";
//    private static String PASSWORD = "V222201B01844CUS001";
    private static String SALT = "InOknmq1C+NCZSi0a9NjIQ==";

    public static String encryptAndEncode(String raw,String pwd) {
        try {
//            PASSWORD=pwd;
//            Log.e("pwd","----"+PASSWORD);
            Cipher c = getCipher(Cipher.ENCRYPT_MODE,pwd);
            byte[] encryptedVal = c.doFinal(getBytes(raw));
            String s = Base64.encodeToString(encryptedVal , Base64.NO_WRAP);

            return s;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static String decodeAndDecrypt(String encrypted,String pwd) throws Exception {
//        byte[] decodedValue = Base64.decodeBase64(getBytes(encrypted));
//        PASSWORD=pwd;
//        Log.e("pwd","----"+PASSWORD);
        byte[] decodedValue = Base64.decode(encrypted.getBytes() , Base64.NO_WRAP);
        Cipher c = getCipher(Cipher.DECRYPT_MODE,pwd);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private static String getString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }

    private static byte[] getBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }

    private static Cipher getCipher(int mode,String pwd) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = getBytes(IV);
        c.init(mode, generateKey(pwd), new IvParameterSpec(iv));
        return c;
    }

    private static Key generateKey(String pwd) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        char[] password = pwd.toCharArray();
        Log.e("pwd","-----"+pwd);
        byte[] salt = getBytes(SALT);

        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        byte[] encoded = tmp.getEncoded();
        return new SecretKeySpec(encoded, "AES");
    }
}
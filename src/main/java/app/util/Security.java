package app.util;

import org.apache.tomcat.util.codec.binary.Base64;


import java.util.Date;

/**
 * Created by Admin on 12.11.2016.
 */
public class Security {
    public static String generateAccessToken(String key){
        String keySource = key+new Date().getTime()*Math.random();
        byte [] tokenByte = new Base64(true).encodeBase64(keySource.getBytes());
        String token = new String(tokenByte);
        return token;
    }
    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }


}

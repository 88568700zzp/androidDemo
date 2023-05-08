package com.jd.lib.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MantoCryptoUtils {

    /* renamed from: a */
    private static String f3749a = null;

    /* renamed from: b */
    private static String f3750b = "AES";

    /* renamed from: c */
    private static String f3751c = "AES/ECB/PKCS5Padding";

    /* renamed from: d */
    private static byte[] f3752d = {ReplyCode.reply0x26, ReplyCode.reply0x38, 11, ReplyCode.reply0x64, -92, -85, 114, -41, -63, 30, ReplyCode.reply0x7b, ReplyCode.reply0x88, ReplyCode.reply0xaa, 102, 10, -32};


    public static String m3783c(String str, String str2) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("6A642D6D");
            sb.append(str2);
            byte[] bytes = sb.toString().getBytes("UTF-8");
            for (int i = 0; i < 16; i++) {
                bytes[i] = (byte) (bytes[i] ^ f3752d[i]);
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("D4F1E5BBBE321897A0F4BDEC91197EE0");
            String sb3 = sb2.toString();
            SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, f3750b);
            Cipher instance = Cipher.getInstance(f3751c);
            instance.init(2, secretKeySpec);
            return new String(instance.doFinal(m3780a(sb3.getBytes("UTF-8"))), "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
            return "";
        } catch (InvalidKeyException e3) {
            e3.printStackTrace();
            return "";
        } catch (IllegalBlockSizeException e4) {
            e4.printStackTrace();
            return "";
        } catch (BadPaddingException e5) {
            e5.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e6) {
            e6.printStackTrace();
            return "";
        }
    }


    /* renamed from: a */
    private static byte[] m3780a(byte[] bArr) {
        if (bArr.length % 2 != 0) {
            return null;
        }
        byte[] bArr2 = new byte[(bArr.length / 2)];
        for (int i = 0; i < bArr.length; i += 2) {
            bArr2[i / 2] = (byte) Integer.parseInt(new String(bArr, i, 2), 16);
        }
        return bArr2;
    }

    /* renamed from: a */
    public static String m3779a(byte[] bArr, byte[] bArr2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "HmacSHA256");
            Mac instance = Mac.getInstance("HmacSHA256");
            instance.init(secretKeySpec);
            return m3782b(instance.doFinal(bArr));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    /* renamed from: b */
    private static String m3782b(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (bArr != null && i < bArr.length) {
            String hexString = Integer.toHexString(bArr[i] & 255);
            if (hexString.length() == 1) {
                sb.append('0');
            }
            sb.append(hexString);
            i++;
        }
        return sb.toString().toUpperCase();
    }

}

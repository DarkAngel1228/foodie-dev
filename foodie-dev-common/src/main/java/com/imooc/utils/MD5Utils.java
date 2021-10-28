package com.imooc.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

public class MD5Utils {

    /**
     * 对字符串进行md5加密
     * @param strValue
     * @return
     * @throws Exception
     */
    public static String getMD5Str(String strValue) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String newStr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));

        return newStr;
    }

}

package com.peirong.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class SendSmsUtil {
    private final static String CHARSET = "UTF-8";
    private final static String SECRET_ID = "AKIDLWSAYhaCk4MfqseBRSDtNCl4A7obCZpq";
    private final static String SECRET_KEY = "rRnwtjOoxrLZYGq88qJWUCfbHhs53pOk";

    public static String sign(String s, String key, String method) throws Exception {
        Mac mac = Mac.getInstance(method);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), mac.getAlgorithm());
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(s.getBytes(CHARSET));
        return DatatypeConverter.printBase64Binary(hash);
    }

    public static String getStringToSign(TreeMap<String, Object> params) {
        StringBuilder s2s = new StringBuilder("GETsms.tencentcloudapi.com/?");
        // 签名时要求对参数进行字典排序，此处用TreeMap保证顺序
        for (String k : params.keySet()) {
            s2s.append(k).append("=").append(params.get(k).toString()).append("&");
        }
        return s2s.toString().substring(0, s2s.length() - 1);
    }

    public static String getUrl(TreeMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder("https://sms.tencentcloudapi.com/?");
        for (String k : params.keySet()) {
            url.append(k).append("=").append(URLEncoder.encode(params.get(k).toString(), CHARSET)).append("&");
        }
        return url.toString().substring(0, url.length() - 1);
    }

    public static String postURL(String phone, String code) throws Exception {
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        // 实际调用时应当使用随机数，例如：params.put("Nonce", new Random().nextInt(java.lang.Integer.MAX_VALUE));
        params.put("Nonce", new Random().nextInt(10000));
        params.put("Timestamp", System.currentTimeMillis() / 1000);
        params.put("SecretId", SECRET_ID);
        params.put("Action", "SendSms");
        params.put("Version", "2021-01-11");
        params.put("Region", "ap-nanjing");
        params.put("SmsSdkAppId", "1400802027");
        params.put("TemplateId", "1749895");
        params.put("TemplateParamSet.0", code);
        params.put("SignName", "EasyDrive公众号");
        params.put("PhoneNumberSet.0", "+86" + phone);
        params.put("SessionContext", "test");
        params.put("Signature", sign(getStringToSign(params), SECRET_KEY, "HmacSHA1"));
        System.out.println("curl '" + getUrl(params) + "'");
        return getUrl(params);
    }
}
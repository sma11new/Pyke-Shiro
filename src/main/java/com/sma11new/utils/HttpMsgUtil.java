package com.sma11new.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author: sma11new
 * @date: 2024/1/31
 * @description: TODO
 */

public class HttpMsgUtil {
    public static void main(String[] args) {
        String rawRequest = "POST /doLogin HTTP/1.1\n" +
                "Host: 192.168.11.100:8080\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:122.0) Gecko/20100101 Firefox/122.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                "Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: 30\n" +
                "Origin: http://192.168.11.100:8080\n" +
                "Connection: close\n" +
                "Referer: http://192.168.11.100:8080/login;jsessionid=2591C485EF42C08BF1C41F7A2D020E8C\n" +
                "Cookie: JSESSIONID=2591C485EF42C08BF1C41F7A2D020E8C\n" +
                "Upgrade-Insecure-Requests: 1\n\n\n";

        Map<String, Object> parsedRequest = parseRawHttpRequest(rawRequest, false, true);
        System.out.println("URL: " + parsedRequest.get("url"));
        System.out.println("Headers: " + parsedRequest.get("headers"));
        System.out.println("Data: " + parsedRequest.get("data"));
    }

    public static Map<String, Object> parseRawHttpRequest(String rawRequest, boolean https, boolean keepCookie) {
        Map<String, Object> result = new HashMap<>();
        String[] requestParts = rawRequest.split("\n\n", 2); // Split headers and body

        // Parse Request Line and Headers
        String[] lines = requestParts[0].split("\n");
        String method = lines[0].split(" ")[0];
        String path = lines[0].split(" ")[1];
        Map<String, String> headers = new HashMap<>();
        String host = "";
        for (int i = 1; i < lines.length; i++) {
            String[] headerParts = lines[i].split(": ", 2);
            headers.put(headerParts[0], headerParts[1]);
            if ("Host".equalsIgnoreCase(headerParts[0])) {
                host = headerParts[1];
            }
        }

        String url;
        if (https) url = "https://" + host + path;
        else url = "http://" + host + path;

        // Parse Data
        Map<String, String> data = new HashMap<>();
        if (requestParts.length > 1 && !requestParts[1].trim().isEmpty()) {
            String[] dataParts = requestParts[1].split("&");
            for (String part : dataParts) {
                String[] keyValue = part.split("=", 2);
                data.put(keyValue[0], keyValue[1]);
            }
        }

        if (!keepCookie) {
            headers.put("Cookie", null);
        } else {
            if (!headers.containsKey("Cookie"))
                headers.put("Cookie", null);
        }

        result.put("method", method);
        result.put("url", url);
        result.put("headers", headers);
        result.put("data", data);

        return result;
    }

    public static String mapToUrlEncodedString(Map<String, String> data) {
        StringJoiner sj = new StringJoiner("&");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            // 对键和值进行URL编码以确保数据的正确传输，这里为了简化示例未进行编码
            // 在实际应用中，你可能需要使用 URLEncoder.encode 方法对这些值进行编码，特别是当值包含空格、特殊字符等时
            String key = entry.getKey();
            String value = entry.getValue();
            sj.add(key + "=" + value);
        }
        return sj.toString();
    }
}

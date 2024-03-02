package com.sma11new.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yhy
 * @date 2021/8/20 22:56
 * @github https://github.com/yhy0
 */

public class Response {
    private int code;
    private String head;
    private HashMap<String, String> headMap;
    private String body;
    private String error;
    private String title;

    public Response() {
    }

    public Response(int code, String head, String body, String error) {
        this.code = code;
        this.head = head;
        this.body = body;
        this.error = error;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getHead() {
        return this.head;
    }
    public void setHeadMap() {
        HashMap<String, String> resultMap = new HashMap<>();
        for (String pair : head.replace("{", "")
                .replace("}", "").split("], ")) {
            String key = pair.split("=\\[")[0];
            String value = pair.split("=\\[")[1];
            resultMap.put(key, value);
        }
        this.headMap = resultMap;
    }
    public Map<String, String> getHeadMap() {
        return this.headMap;
    }

    public void setHead(String head) {
        this.head = head;
        setHeadMap();
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
        setTitle();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle() {
        Document document = Jsoup.parse(this.body);
        this.title = document.title();
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }
}


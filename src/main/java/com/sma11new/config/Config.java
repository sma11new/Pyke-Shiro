package com.sma11new.config;

import cn.hutool.core.io.resource.ResourceUtil;

public class Config {
    public static String NAME = "Pyke-Shiro";
    public static String VERSION = "0.3";
    // 更新日志
    public static String UPDATEINFO = ResourceUtil.readUtf8Str("updateInfo.txt");
    // 默认编码
    public static String DEFAULT_ENCODING = "UTF-8";
    // 默认请求超时
    public static int CONNECT_TIME_OUT = 5;
    // 默认请求超时
    public static int READ_TIME_OUT = 10;
}

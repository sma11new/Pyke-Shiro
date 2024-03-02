package com.sma11new.exp.shiro.attack.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassFiles {
    public static String classAsFile(Class<?> clazz) {
        return ClassFiles.classAsFile(clazz, true);
    }

    public static String classAsFile(Class<?> clazz, boolean suffix) {
        String str = clazz.getEnclosingClass() == null ? clazz.getName().replace(".", "/") : ClassFiles.classAsFile(clazz.getEnclosingClass(), false) + "$" + clazz.getSimpleName();
        if (suffix) {
            str = str + ".class";
        }
        return str;
    }

    public static byte[] classAsBytes(Class<?> clazz) {
        try {
            int len;
            byte[] buffer = new byte[1024];
            String file = ClassFiles.classAsFile(clazz);
            InputStream in = ClassFiles.class.getClassLoader().getResourceAsStream(file);
            if (in == null) {
                throw new IOException("couldn't find '" + file + "'");
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


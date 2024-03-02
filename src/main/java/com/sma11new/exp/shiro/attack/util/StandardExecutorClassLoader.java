package com.sma11new.exp.shiro.attack.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class StandardExecutorClassLoader
extends URLClassLoader {
    private static final String baseDir = System.getProperty("user.dir") + File.separator + "lib" + File.separator;

    public StandardExecutorClassLoader(String version) {
        super(new URL[0], (ClassLoader)null);
        this.loadResource(version);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            return StandardExecutorClassLoader.class.getClassLoader().loadClass(name);
        }
    }

    private void loadResource(String version) {
        String jarPath = baseDir + version;
        this.tryLoadJarInDir(jarPath);
    }

    private void tryLoadJarInDir(String dirPath) {
        System.out.println("Try load jar in dir: " + dirPath);
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (!file.isFile() || !file.getName().endsWith(".jar")) continue;
                this.addURL(file);
            }
        }
    }

    private void addURL(File file) {
        try {
            super.addURL(new URL("file", null, file.getCanonicalPath()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


package com.xiaohongxiedaima.demo.javacore.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CustomClassLoader extends ClassLoader {

    private String rootDir;

    public CustomClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }

    private byte[] read(String className) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(rootDir + File.separator + className + ".class");
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        return bytes;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        try {
            byte[] bytes = this.read(className);
            Class<?> aClass = super.defineClass(className, bytes, 0, bytes.length);
            return aClass;
        } catch (Throwable throwable) {
            throw new ClassNotFoundException(className);
        }
    }
}

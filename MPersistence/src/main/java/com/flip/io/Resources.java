package com.flip.io;

import java.io.InputStream;

public class Resources {
    //加载配置文件 -> 字节输入流 -> 内存
    public static InputStream getResourceAsStream(String path) {
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}

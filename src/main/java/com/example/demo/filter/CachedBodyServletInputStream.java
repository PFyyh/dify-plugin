package com.example.demo.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 缓存的 ServletInputStream 实现
 * 用于包装已缓存的请求体数据
 * 实现了 ServletInputStream 的所有必要方法
 * 使其能够像普通的输入流一样被使用
 */
public class CachedBodyServletInputStream extends ServletInputStream {
    /** 缓存的请求体输入流 */
    private ByteArrayInputStream cachedBodyInputStream;

    /**
     * 构造函数
     * @param cachedBody 已缓存的请求体字节数组
     */
    public CachedBodyServletInputStream(byte[] cachedBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
    }

    /**
     * 检查是否已读取完所有数据
     * @return 如果没有更多数据可读则返回 true
     */
    @Override
    public boolean isFinished() {
        return cachedBodyInputStream.available() == 0;
    }

    /**
     * 检查是否可以读取数据
     * @return 始终返回 true，表示随时可以读取
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * 设置读取监听器
     * 由于是内存中的数据，不需要异步读取，所以不支持此操作
     */
    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException();
    }

    /**
     * 读取下一个字节
     * @return 读取到的字节，如果到达流末尾则返回 -1
     */
    @Override
    public int read() throws IOException {
        return cachedBodyInputStream.read();
    }
}
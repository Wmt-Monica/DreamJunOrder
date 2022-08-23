package com.dreamplume.sell.util.generator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Classname IDGenerator
 * @Description TODO
 * @Date 2022/4/20 15:34
 * @Created by 翊
 */
// 单例模式
public class IDGenerator {

    public static IDGenerator instance;

    private AtomicLong id = new AtomicLong();

    private IDGenerator() {}

    public static IDGenerator getInstance() {
        if (instance == null) {
            synchronized(IDGenerator.class) { // 此处为类级别的锁
                if (instance == null) {
                    instance = new IDGenerator();
                }
            }
        }
        return instance;
    }
    public String getId() {
        return System.currentTimeMillis() + String.valueOf(id.incrementAndGet());
    }
}

package com.ww.rabbitmq.utils;

/**
 * @author: Sun
 * @create: 2021-09-01 17:05
 * @version: v1.0
 */
public class SleepUtil {

    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
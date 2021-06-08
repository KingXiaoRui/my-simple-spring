package com.king.demo.test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenxiaorui
 * @description Demo
 * @date 2021/3/25
 */
public class Demo {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock(true);
        try {
            lock.lock();
        } finally {
            lock.unlock();
        }

    }

}

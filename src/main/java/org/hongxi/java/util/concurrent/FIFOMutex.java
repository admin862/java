package org.hongxi.java.util.concurrent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @author shenhongxi 2019/09/01
 * 公平(先进先出)互斥锁
 */
public class FIFOMutex {

    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);

        // 只有队首的线程可以获取锁
        while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            // 忽略中断，并重置中断标记，然后继续while判断
            if (Thread.interrupted())
                wasInterrupted = true;
        }

        waiters.remove();
        /**
         * 其他线程中断了该线程，虽然我对中断信号不感兴趣，忽略它，
         * 但是不代表其他线程对该标识不感兴趣，所以要恢复下
         */
        if (wasInterrupted)
            current.interrupt();
    }

    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }
}

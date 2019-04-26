package com.inext.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6 0006.
 */
public class LockUtils {
    private static List<String> list = Collections.synchronizedList(new ArrayList<String>());

    /**
     * 加锁
     *
     * @param account
     */
    public static void lock(String account) {

        synchronized (list) {

            while (list.contains(account)) {

                try {

                    //System.out.println("wait");

                    list.wait();

                    //System.out.println("wakes up");

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                    return;

                }

            }

            list.add(account);


        }

    }

    ;

    /**
     * 解锁
     *
     * @param account
     */
    public static void unlock(String account) {

        synchronized (list) {

            list.remove(account);

            list.notifyAll();

        }

    }

}

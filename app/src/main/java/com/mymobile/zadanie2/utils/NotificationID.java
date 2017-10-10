package com.mymobile.zadanie2.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tomek on 08.10.17.
 */

public class NotificationID {
    private static AtomicInteger c;

    public static void setBeginning(int begin) {
        c = new AtomicInteger(begin);
    }

    public static int getID() {
        return c.getAndIncrement();
    }

    public static int getWithoutInc() {
        return c.get();
    }
}

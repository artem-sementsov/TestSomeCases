package org.asementsov.samples;

public class Singleton {

    private static Singleton instance;

    private Singleton() {

    }

    // synchronized with synchronization block
    public static Singleton getInstance() {

        synchronized (Singleton.class) {
            if (instance == null) {
                instance = new Singleton();
            }
        }

        return instance;
    }

    // synchronized
//    public static synchronized Singleton getInstance() {
//
//        if (instance == null) {
//            instance = new Singleton();
//        }
//
//        return instance;
//    }

    // not synchronized
//    public static Singleton getInstance() {
//
//        if (instance == null) {
//            instance = new Singleton();
//        }
//
//        return instance;
//    }

    public static void clear() {
        instance = null;
    }
}

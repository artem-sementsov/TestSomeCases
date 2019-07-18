package org.asementsov.samples;

public class Singleton {

    private static Singleton instance;

    private Singleton(){

    }

    // synchronized or not
    public static Singleton getInstance() {

        if (instance == null) {
            instance = new Singleton();
        }

        return instance;
    }

    public static void clear() {
        instance = null;
    }
}

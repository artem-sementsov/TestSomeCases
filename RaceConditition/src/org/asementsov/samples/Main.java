package org.asementsov.samples;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {

        int count = 0;
        long ts = System.currentTimeMillis();
        long tsAvg = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        start:
        while (true) {
            ++count;

            List<Future<Singleton>> list = new ArrayList<>();

            for (int i = 0; i < 2; i++) {
                Future<Singleton> future = executor.submit(Singleton::getInstance);
                list.add(future);
            }

            Singleton result = null;

            for (Future<Singleton> fut : list) {
                try {
                    Singleton cur = fut.get();
                    if (result == null) {
                        result = cur;
                    } else {
                        if (result != cur) {
                            System.out.println("result = " + result);
                            System.out.println("cur = " + cur);
                            System.out.println("it happens once per " + count + " attempts");

                            throw new ConcurrentModificationException();
                        } else {
                            //System.out.println("no race condition");
                        }
                    }
                } catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Average execution time = " + tsAvg);
                    break start;
                }
            }

            Singleton.clear();

            if (count % 1000 == 0) {
                long tsCur = System.currentTimeMillis();
                long diff = tsCur - ts;

                ts = tsCur;

                if (tsAvg != 0) {
                    tsAvg = (tsAvg + diff) / 2;
                } else {
                    tsAvg = diff;
                }
                System.out.println("Average execution time = " + tsAvg);
            }
        }
        executor.shutdown();
    }
}
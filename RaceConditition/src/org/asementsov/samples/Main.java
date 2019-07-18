package org.asementsov.samples;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int count = 0;
        long ts = System.currentTimeMillis();
        long tsAvg = 0;

        while (true) {
            ++count;
            ExecutorService executor = Executors.newFixedThreadPool(2);

            List<Future<Singleton>> list = new ArrayList<>();

            Callable<Singleton> callable = new SingletonCallable();
            for (int i = 0; i < 2; i++) {
                //submit Callable tasks to be executed by thread pool
                Future<Singleton> future = executor.submit(callable);
                //add Future to the list, we can get return value using Future
                list.add(future);
            }

            Singleton result = null;

            for (Future<Singleton> fut : list) {
                try {
                    //print the return value of Future, notice the output delay in console
                    // because Future.get() waits for task to get completed
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
                    System.exit(1);
                }
            }
            //shut down the executor service now
            executor.shutdown();

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
                //System.out.println("Average execution time = " + tsAvg);
            }
        }
    }
}

class SingletonCallable implements Callable {

    @Override
    public Object call() throws Exception {
        return Singleton.getInstance();
    }
}
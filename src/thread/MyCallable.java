package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
            System.out.println("当前线程：" + Thread.currentThread().getName() + " " + sum);
        }
        return sum;
    }

    public static void main(String[] args) {
        MyCallable myCallable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(myCallable);
        FutureTask<Integer> futureTask1 = new FutureTask<>(myCallable);
        new Thread(futureTask).start();
        new Thread(futureTask1).start();
        try {
            Integer sum1 = futureTask.get();//等待这个结果，直到这个结果产生
            Integer sum2 = futureTask1.get();
            System.out.println("sum1=" + sum1);
            System.out.println("sum2=" + sum2);
            System.out.println("总和为：" + (sum1 + sum2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

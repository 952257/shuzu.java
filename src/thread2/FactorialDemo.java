package thread2;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 编程题2：分别用继承 Thread、实现 Runnable、实现 Callable
 * 创建三个线程并发计算阶乘，在 main 中打印各自结果
 */
public class FactorialDemo {

    /** 方式一：继承 Thread */
    static class FactorialThread extends Thread {
        private final int n;
        private long result;

        FactorialThread(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            result = factorial(n);
        }

        long getResult() {
            return result;
        }
    }

    /** 方式二：实现 Runnable */
    static class FactorialRunnable implements Runnable {
        private final int n;
        private long result;

        FactorialRunnable(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            result = factorial(n);
        }

        long getResult() {
            return result;
        }
    }

    /** 方式三：实现 Callable */
    static class FactorialCallable implements Callable<Long> {
        private final int n;

        FactorialCallable(int n) {
            this.n = n;
        }

        @Override
        public Long call() {
            return factorial(n);
        }
    }

    static long factorial(int n) {
        long r = 1;
        for (int i = 2; i <= n; i++) {
            r *= i;
        }
        return r;
    }

    public static void main(String[] args) throws Exception {
        int n1 = 5, n2 = 6, n3 = 7;

        FactorialThread t1 = new FactorialThread(n1);
        FactorialRunnable r2 = new FactorialRunnable(n2);
        Thread t2 = new Thread(r2);
        FutureTask<Long> task = new FutureTask<>(new FactorialCallable(n3));
        Thread t3 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        long result3 = task.get();

        System.out.println("Thread 计算 " + n1 + "! = " + t1.getResult());
        System.out.println("Runnable 计算 " + n2 + "! = " + r2.getResult());
        System.out.println("Callable 计算 " + n3 + "! = " + result3);
    }
}

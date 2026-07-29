package thread;

import java.util.concurrent.*;

/**
 * 假设每件事情3秒钟
 * 洗脸
 * 刷牙
 * 烧水
 * 煮鸡蛋
 * 吃鸡蛋
 *
 * 怎么安排这些事情，使得总时间最少
 *
 * (洗脸,烧水,煮鸡蛋)->刷牙->吃鸡蛋
 *
 * 同步 辩论会的辩论
 * 异步 吵架
 *
 */
public class 起床 {

    static final int 时间 = 3000;

    public static void 洗脸(){
        try {
            Thread.sleep(时间);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("洗脸");
    }
    public static void 刷牙(){
        try {
            Thread.sleep(时间);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("刷牙");
    }
    public static void 烧水(){
        try {
            Thread.sleep(时间);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("烧水");
    }
    public static void 煮鸡蛋(){
        try {
            Thread.sleep(时间);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("煮鸡蛋");
    }
    public static void 吃鸡蛋(){
        try {
            Thread.sleep(时间);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("吃鸡蛋");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        long start = System.currentTimeMillis();

        // (洗脸, 烧水, 煮鸡蛋) 三个任务同时进行
        Future<?> f1 =  pool.submit(起床::洗脸);
        Future<?> f2 =  pool.submit(起床::烧水);
        Future<?> f3 = pool.submit(起床::煮鸡蛋);
        f1.get();
        f2.get();
        f3.get();

        // 串行：刷牙 -> 吃鸡蛋
        刷牙();
        吃鸡蛋();

        long end = System.currentTimeMillis();
        System.out.println("总时间：" + (end - start) + "ms");
        pool.shutdown();
    }
}

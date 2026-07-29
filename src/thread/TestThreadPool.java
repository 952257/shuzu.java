package thread;

import java.util.concurrent.*;

/**
 * 1.并发任务优先交给核心线程(老不死)处理，如果核心线程都忙乎着呢，再交给阻塞队列
 * 2.如果阻塞队列也满了，再交给非核心线程(闲置一段时间就会销毁)处理
 * 3.如果非核心线程也忙乎着呢，再交给拒绝策略处理
 */
public class TestThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                5,//核心线程数
                10,//最大线程数
                60,//存活时间
                TimeUnit.SECONDS,//时间单位
                new LinkedBlockingQueue<>(5),//阻塞队列
                Executors.defaultThreadFactory(),//线程工厂
//                new ThreadPoolExecutor.AbortPolicy()//拒绝策略
                new ThreadPoolExecutor.DiscardPolicy()
//                new ThreadPoolExecutor.CallerRunsPolicy()//调度者自己执行
//                new ThreadPoolExecutor.DiscardOldestPolicy()//丢弃最老的任务
        );

//        SumRunnableFinal sumRunnableFinal1 = new SumRunnableFinal(1, 100);
//        SumRunnableFinal sumRunnableFinal2 = new SumRunnableFinal(101, 200);
//        SumRunnableFinal sumRunnableFinal3 = new SumRunnableFinal(201, 300);
//        SumRunnableFinal sumRunnableFinal4 = new SumRunnableFinal(301, 400);
//        SumRunnableFinal sumRunnableFinal5 = new SumRunnableFinal(401, 500);
//        SumRunnableFinal sumRunnableFinal6 = new SumRunnableFinal(501, 600);
//        SumRunnableFinal sumRunnableFinal7 = new SumRunnableFinal(601, 700);
//        SumRunnableFinal sumRunnableFinal8 = new SumRunnableFinal(601, 700);
//        SumRunnableFinal sumRunnableFinal9 = new SumRunnableFinal(601, 700);
//        SumRunnableFinal sumRunnableFinal10 = new SumRunnableFinal(601, 700);
//        SumRunnableFinal sumRunnableFinal11 = new SumRunnableFinal(601, 700);
//        pool.execute(sumRunnableFinal1);
//        pool.execute(sumRunnableFinal2);
//        pool.execute(sumRunnableFinal3);
//        pool.execute(sumRunnableFinal4);
//        pool.execute(sumRunnableFinal5);
//        pool.execute(sumRunnableFinal6);
//        pool.execute(sumRunnableFinal7);
//        pool.execute(sumRunnableFinal8);
//        pool.execute(sumRunnableFinal9);
//        pool.execute(sumRunnableFinal10);
//        pool.execute(sumRunnableFinal11);
//        SumCallableFinal sumCallableFinal1 = new SumCallableFinal(1, 100);
//        SumCallableFinal sumCallableFinal2 = new SumCallableFinal(101, 200);
//        Future<Integer> future1 = pool.submit(sumCallableFinal1);
//        Future<Integer> future2 = pool.submit(sumCallableFinal2);
//        Integer result1 = future1.get();//等待
//        Integer result2 = future2.get();//等待
//        System.out.println(result1+result2);

        //阿里不建议高并发的情况下使用
        //缓存线程池 OOM OutOfMemoryError 线程爆炸
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        //单例线程池 队列会爆炸 OOM OutOfMemoryError
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        //单例线程池 队列会爆炸 OOM OutOfMemoryError
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        //定时线程池
        ExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
    }
}

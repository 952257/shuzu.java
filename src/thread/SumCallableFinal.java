package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class SumCallableFinal implements Callable<Integer> {
    private final int start;
    private final int end;

    public SumCallableFinal(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = start; i <= end; i++) {
            sum += i;
            System.out.println("当前线程：" + Thread.currentThread().getName() + " " + sum);
        }
        System.out.println(start + "-" + end + "的和为：" + sum);
        return sum;
    }

    public static void main(String[] args) {
        SumCallableFinal sumCallableFinal = new SumCallableFinal(1, 100);
        SumCallableFinal sumCallableFinal1 = new SumCallableFinal(101, 200);
        FutureTask<Integer> futureTask = new FutureTask<>(sumCallableFinal);
        FutureTask<Integer> futureTask1 = new FutureTask<>(sumCallableFinal1);
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


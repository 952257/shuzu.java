/**
 * 求和
 * 第一个线程计算1-100的和
 * 第二个线程计算101-200的和
 * 要求并发
 * 最后计算这两个和之和
 *
 * 自定义线程类
 * 继承thread
 * 重写run方法
 * 调用自己的start方法启动线程
 */
public class MyThread extends Thread{
    static int sum1;
    static int sum2;

    /**
     * 并发任务
     */
    @Override
    public void run() {
        int sum = 0;
        if (getName().equals("t1")) {
            for (int i = 1; i <= 100; i++) {
                sum += i;
            }
            sum1 = sum;
        } else {
            for (int i = 101; i <= 200; i++) {
                sum += i;
            }
            sum2 = sum;
        }
        System.out.println(Thread.currentThread().getName() + " " + sum);
    }

    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();
        t1.setName("t1");
        t2.setName("t2");
        t1.start();//启动线程
        t2.start();//启动线程
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(sum1 + sum2);
    }
}

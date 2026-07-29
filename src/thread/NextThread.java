package thread;

public class NextThread implements Runnable {
    int start;
    int end;
    int sum;

    public NextThread(int start, int end) {
        this.start = start;
        this.end = end;
    }
    @Override
    public void run() {
        for (int i = start; i <= end; i++) {
            sum += i;
        }
        System.out.println(Thread.currentThread().getName() + " " + sum);
    }
    public static void main(String[] args) {
        NextThread r1 = new NextThread(1, 100);
        NextThread r2 = new NextThread(101, 200);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();//启动线程
        t2.start();//启动线程
        System.out.println("总和为：" + (r1.sum + r2.sum));
    }
}

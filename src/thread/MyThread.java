package thread;
public class MyThread extends Thread {
    final int start;
    final int end;
    int sum;

    public MyThread(int start, int end) {
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
        MyThread t1 = new MyThread(1, 100);
        MyThread t2 = new MyThread(101, 200);
        t1.start();//启动线程
        t2.start();//启动线程
        try{
            t1.join();//等待t1算完
            t2.join();//等待t2算完
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("总和为：" + (t1.sum + t2.sum));
    }
}

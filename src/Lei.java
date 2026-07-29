import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

public class Lei {
    static void main() {
//        String name = "your father";
//        int age = 18;
//        System.out.println(name + "年龄" + age);
//        int chang = 10;
//        int kuan = 12;
//        int zong = chang * kuan;
//        System.out.println(zong);
//        System.out.println(chang + kuan);
//        for(int i=1; i <= 10 ;i++){
//            System.out.println("我是你father");
//            System.out.println("your father的年龄是"+(age + i));
//        }
//        Scanner sc = new Scanner(System.in);
//        System.out.println("请输入ab的值");
//        double a = sc.nextDouble();
//        double b = sc.nextDouble();
//        System.out.println(a*b);
//        int c = 7;
//        int d = 3;
//        c = c + d;
//        d = c - d;
//        c = c - d;
//        System.out.println(c);
//        System.out.println(d);
//        String left,right,dock;
//        left = "red";
//        right = "blue";
//        dock = left;
//        left = right;
//        right = dock;
//        System.out.println(left);
//        System.out.println(right);
//        int ni = sc.nextInt();
//        if (ni > 10) {
//            System.out.println("我是你爹");
//        }
//        else {
//            System.out.println("我还是你爹");
//        }
//        int n2 = 1;
//        do{
//            System.out.println("我是你爹");
//            n2++;
//        }while(n2 <= 10);
//        System.out.println(n2);
//        int n = 10,m = 3;
//        System.out.println(n < m ? n : m);
//        System.out.println((n + m) % 3 != 0 && (n - m) % 3 == 0 ? n : m);
//        System.out.println(n < 100 || --m > 0 ? n : m);
//        System.out.println(n % m == 1 ? (n+=m) : (n-=m));
//        int x = 10;
//        x = (x += 10) > 10 ? 10 - x++ : --x + 10;
//        System.out.println(x);
//        int y = 20;
//        y = (y /= 3 == 0 ? 10 + y-- : --y - 10);
//        System.out.println(y);
//        for (int i = 1; i <= 9; i++) {
//            for (int j = 1; j <= i; j++) {
//                System.out.print(j + "×" + i + "=" + (i * j) + "\t");
//            }
//            System.out.println();
//        }
//        int [] ax = new int [2];
//        ax[0] = 1;
//        ax[1] = 2;
//        System.out.println(ax[0]);
//        System.out.println(ax[1]);
//
//        String[] arr = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
//        String[] newArr = new String[arr.length];
//        for (int i = arr.length -1; i >= 0; i--) {
//            newArr[arr.length -i - 1] = arr[i];
//        }
//        for (String str : newArr) {
//            System.out.println(str);
//        }
//
//        int[] arr1 = {2,5,8,9,1};
//        int max = arr1[0];
//        for (int i = 1; i < arr1.length; i++) {
//            if(arr1[i] > max){
//                max = arr1[i];
//            }
//        }
//        System.out.println(max);
//
//        String[] arr2 = {"a", "b", "c"};
//        String[] arr3 = {"d", "e", "f"};
//
//        String[] newArr1 = new String[arr2.length + arr3.length];
//        for (int i = 0; i < arr2.length; i++) {
//            newArr1[i] = arr2[i];
//        }
//        for (int i = 0; i < arr3.length; i++) {
//            newArr1[arr2.length + i] = arr3[i];
//        }
//        for (int i = 0; i < newArr1.length; i++) {
//            System.out.println(newArr1[i]);
//        }
        int[] nums = {64, 34, 25, 12, 22, 11, 90};
        // 外层循环：控制比较的轮数，一共需要 nums.length-1 轮
        for (int i = 0; i < nums.length - 1; i++) {
            // 内层循环：两两比较。每一轮过后，最大的数都会被移到最后
            // 所以每一轮都可以少比较 i 个元素（即 nums.length - i - 1）
            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    // 交换 nums[j] 和 nums[j+1]
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(nums));
    }
}

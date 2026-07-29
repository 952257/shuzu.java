import java.util.Arrays;
public class FangFa2 {
    public static void main(String[] args) {
        /**
         * 形参：形式上的参数，定义方法时的参数，此时形参没有值
         * 实参：调用方法传的参数，对形参进行赋值
         */

        //getSum(1, 50);
        //int i = 1;
        //method1(i);
        //System.out.println(i); // 1

        //int[] arr = {1, 2, 3};
        //method2(arr);
        //System.out.println(Arrays.toString(arr)); // 100, 2, 3
        String str = "1";
        method3(str);

        // 多次调用方法，都是在各自内存中进行操作，互不干扰
        method3(str);
        System.out.println("main：" + str); // "2"

        String str2 = "1";
        System.out.println(str == str2);

    }

//    public static void getSum(int num1, int num2){
//        int sum = 0;
//        for (int i = num1; i <= num2; i++) {
//            sum += i;
//        }
//        System.out.println(sum);
//    }

    public static void method1(int i){
        // int i = 1;
        // in是基本数据类型，存在方法的栈内存中，修改形参不会影响实参
        i = 100;
        System.out.println(i); // 100
    }

    public static void method2(int[] arr){
        // 数组是引用类型，实参和形参指向的都是同一个数组对象，方法中对数组的赋值，会影响方法外部的数组
        arr[0] = 100;
        System.out.println(Arrays.toString(arr)); // 100, 2, 3
    }

    public static void method3(String str){
        /*
            字符串是引用类型，它的值是存在堆内存常量池中，方法中对字符串的赋值，会重新创建一个字符串对象,
            并赋值给形参str，不会影响方法外部的字符串
        */
        str = "2";
        // 方法内部定义的变量，叫做局部变量，方法的外部是无法使用的，只能在方法内部使用
        int i1 = 100;
        System.out.println("method3：" + str); // "2"
    }
}

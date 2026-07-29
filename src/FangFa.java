public class FangFa {
    public static void main(String[] args) {
        getSum1(1, 100);

        int i = getSum(1, 50);
        System.out.println(i);

        //getSum(1, 100);
        // 计算1-50累加和
//        int sum = 0;
//        for (int i = 1; i <= 50; i++) {
//            sum += i;
//        }
//        System.out.println(sum);

        // 计算1-100累加和
//        int sum1 = 0;
//        for (int i = 1; i <= 100; i++) {
//            sum1 += i;
//        }
//        System.out.println(sum1);

        /**
         * 方法：对一段具有相同功能性的代码，写到方法中，以便于下次直接调用重复使用
         * 通用语法定义
         *  修饰符 返回结果类型 方法名(参数){
         *      方法体
         *      retrun 结果;
         *  }
         *
         * 代码解释：
         *  修饰符：public static（先写固定，后续的学习中再讲）
         *  返回结果类型：方法执行结束后，返回的数据
         *  方法名：见名知意（小驼峰）
         *  参数：进入方法内部参与执行的数据
         *  方法体：实现方法的代码
         *  retrun 结果：如果这个方法有返回值，则通过retrun把结果进行返回
         *
         *  方法的定义的方式：
         *      -有参数，有返回值
         *      -有参数，无返回值
         *      -无参数，有返回值
         *      -无参数，无返回值
         *
         *  总结：
         *      1.java的方法不能嵌套方法
         *      2.方法不会自己调用，必须通过 方法名()调用，main它是访问的入口，是由jvm调用
         *      3.方法的返回结果数据类型必须要与方法返回结果类型保持一致
         *      4.方法的执行顺序跟书写的代码顺序无关，只跟调用的顺序有关
         *      5.如果一个方法有返回结果类型，那么必须要进行return 结果（两者必须同时存在）;
         *      6.void表示该方法没有返回值，那么就不能使用return 结果
         */
    }

    //有参数，有返回值
    public static int getSum(int num1, int num2){
        int sum = 0;
        for (int i = num1; i <= num2; i++) {
            sum += i;
        }
        // System.out.println(sum);
        return sum;
    }

    //有参数，无返回值
    public static void getSum1(int num1, int num2){
        int sum = 0;
        for (int i = num1; i <= num2; i++) {
            sum += i;
        }
        System.out.println(sum);
    }

    // 无参数，有返回值
    public static int getSum2(){
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println(sum);
        return sum;
    }

    // 无参数，无返回值
    public static void getSum3(){
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println(sum);
    }

}

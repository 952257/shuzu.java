import java.util.Arrays;
import java.util.Random;

public class RandomFive {
    public static void main(String[] args) {
        String[] questions = {
            "列出八种基本数据类型及字节数",
            "面向对象三大特征",
            "static关键字的作用",
            "什么是方法的重载(Overload)",
            "final关键字的作用",
            "什么是方法的重写(Override)？",
            "==和equals区别",
            "写出5个String类的常用方法和其作用",
            "String、StringBuilder、StringBuffer区别",
            "抽象类和接口各自特点？",
            "int和Integer区别",
            "浅复制和深复制的区别",
            "ArrayList和LinkedList区别",
            "List和Set区别",
            "写出5个HashMap的方法和作用",
            "Java中有哪些常用的集合类(数据结构)?",
            "ArrayList实现原理?",
            "写出HashMap(散列表)的实现原理",
            "常用的IO流有哪些？",
            "写出线程的四种创建方式",
            "写出JDK自定义线程池的七大参数及作用",
            "如何处理不安全的线程(并发安全)",
            "Java内置的线程池有哪几种",
            "什么是反射机制",
            "Mysql常用的函数有哪些？",
            "数据库内连接和外连接区别",
            "数据库设计的范式和反范式是什么？",
            "数据库事务四大特性（ACID）?",
            "什么是索引？索引的作用？索引是否越多越好？为什么？",
            "mysql中索引有哪几种(写出5种)",
            "什么是复合索引，什么是最左匹配原则",
            "mysql事务隔离级别有哪几种?默认用的是哪个级别?",
            "数据库索引B树和B+树区别？",
            "mysql分页查询如何优化",
            "Stream 流式编程常用方法",
            "git常用命令",
            "git遇到冲突怎么解决",
            "写出SpringBoot中以下注解的作用（@RestController、@RequestMapping、@PostMapping、@PathVariable、@RequestBody）",
            "写出SpringBoot中以下注解的作用（@ControllerAdvice、@ExceptionHandler、@Resource、@Service、@SpringBootApplication）",
            "常见的Http请求类型有哪些?",
            "mybatis中的sql，使用#和$区别",
            "mybatis中，常用的动态sql标签有哪些？",
            "spring中事务失效场景",
            "什么是IoC和AOP，作用分别是什么？",
            "在spring中，注册bean几种方式？依赖注入有几种方式?",
            "AOP应用场景有哪些？",
            "在springAOP中，通知类型有哪几种?",
            "JDK动态代理和CGLIB动态代理区别",
            "HTTP的状态码有哪些？",
            "SpringBoot常用的starter有哪些?",
            "SpringBoot自动装配原理(流程)"
        };

        int[] numbers = new int[5];
        Random random = new Random();
        int count = 0;

        while (count < 5) {
            int num = random.nextInt(51) + 1;
            boolean exists = false;
            for (int i = 0; i < count; i++) {
                if (numbers[i] == num) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                numbers[count] = num;
                count++;
            }
        }

        Arrays.sort(numbers);
        System.out.println("随机抽取的5道题：");
        for (int i = 0; i < numbers.length; i++) {
            int num = numbers[i];
            System.out.println(num + ". " + questions[num - 1]);
        }
    }
}

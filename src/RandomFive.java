import java.util.Arrays;
import java.util.Random;

public class RandomFive {
    public static void main(String[] args) {
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
        System.out.println("随机抽取的5个数字（1-51）：" + Arrays.toString(numbers));
    }
}

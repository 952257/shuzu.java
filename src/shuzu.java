import java.util.Arrays;

public class shuzu
{
    public static void main(String[] args)
    {
        int[] shuZu = {3, 0, 8, 5, 2};

        // 1. 计算所有元素的和
        int he = 0;
        for (int i = 0; i < shuZu.length; i++)
        {
            he += shuZu[i];
        }
        System.out.println("数组元素的和：" + he);

        // 2. 找出数组中的最大值
        int zuiDaZhi = shuZu[0];
        for (int i = 1; i < shuZu.length; i++)
        {
            if (shuZu[i] > zuiDaZhi)
            {
                zuiDaZhi = shuZu[i];
            }
        }
        System.out.println("数组中的最大值：" + zuiDaZhi);

        // 3. 实现数组的反转
        int[] fanZhuan = new int[shuZu.length];
        for (int i = 0; i < shuZu.length; i++)
        {
            fanZhuan[i] = shuZu[shuZu.length - 1 - i];
        }
        System.out.println("反转后的数组：" + Arrays.toString(fanZhuan));

        // 4. 计算数组元素的平均值
        int zongHe = 0;
        for (int i = 0; i < shuZu.length; i++)
        {
            zongHe += shuZu[i];
        }
        double pingJunZhi = (double) zongHe / shuZu.length;
        System.out.println("数组元素的平均值：" + pingJunZhi);

        // 5. 统计数组中偶数的个数
        int ouShuGeShu = 0;
        for (int i = 0; i < shuZu.length; i++)
        {
            if (shuZu[i] % 2 == 0)
            {
                ouShuGeShu++;
            }
        }
        System.out.println("数组中偶数的个数：" + ouShuGeShu);

        // 6. 将数组中的所有元素乘以2
        int[] chengYiEr = new int[shuZu.length];
        for (int i = 0; i < shuZu.length; i++)
        {
            chengYiEr[i] = shuZu[i] * 2;
        }
        System.out.println("所有元素乘以2后的数组：" + Arrays.toString(chengYiEr));

        // 8. 将数组中的0移到末尾，保持其他元素的相对顺序
        int[] lingDaoMoWei = new int[shuZu.length];
        int weiZhi = 0;
        for (int i = 0; i < shuZu.length; i++)
        {
            if (shuZu[i] != 0)
            {
                lingDaoMoWei[weiZhi] = shuZu[i];
                weiZhi++;
            }
        }
        for (int i = weiZhi; i < lingDaoMoWei.length; i++)
        {
            lingDaoMoWei[i] = 0;
        }
        System.out.println("将0移到末尾后的数组：" + Arrays.toString(lingDaoMoWei));
    }
}

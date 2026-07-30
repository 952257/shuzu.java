import org.junit.Assert;
import org.junit.Test;

public class TestMyClass {
    @Test
    public void test() {
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        Assert.assertArrayEquals(a, b);
        Assert.assertEquals(1, 1);
    }
}

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeapsortTest {

    @Test
    public void heapsort() {
        int [] testingArr1 = {5, 4, 3, 2, 1};
        Heap.heapsort(testingArr1);
        int [] expectedArr1 = {1,2,3,4,5};
        Assert.assertArrayEquals(expectedArr1, testingArr1);
        //
        int [] testingArr2 = {1, 15, 4, 2, 3, -1, 0, -99, 100, 4, 5, 6, -8};
        Heap.heapsort(testingArr2);
        int [] expectedArr2 = {-99, -8, -1, 0, 1, 2, 3, 4, 4, 5, 6, 15, 100};
        Assert.assertArrayEquals(expectedArr2, testingArr2);
        //
        int [] testingArr3 = {15, 4, -1, 3, 100, 100500, -123, 5, 5, 5, 18, -404, 505, 112};
        Heap.heapsort(testingArr3);
        int [] expectedArr3 = {-404, -123, -1, 3, 4, 5,5,5, 15, 18, 100, 112, 505, 100500};
        Assert.assertArrayEquals(expectedArr3, testingArr3);
        //
        int [] testingArr4 = {1,1,1,1,1,1,1};
        Heap.heapsort(testingArr4);
        int [] expectedArr4 = {1,1,1,1,1,1,1};
        Assert.assertArrayEquals(expectedArr4, testingArr4);
    }
}
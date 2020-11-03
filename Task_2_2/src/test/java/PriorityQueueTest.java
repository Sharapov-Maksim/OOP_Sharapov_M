import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class PriorityQueueTest {

    @Test
    public void BasicTest() {
        PriorityQueue<Integer, String> queue = new PriorityQueue<Integer, String>();
        queue.insert(200, "собака");
        queue.insert(10, "человек");
        Assert.assertEquals("собака", queue.extractmax());
        queue.insert(5, "пингвин");
        queue.insert(500, "попугай");
        Assert.assertEquals("попугай", queue.extractmax());
        Assert.assertEquals("человек", queue.extractmax());
        Assert.assertEquals("пингвин", queue.extractmax());
    }

    @Test
    public void iteratorTest() {
        String[] expectedRes = {"попугай", "собака", "пингвин", "человек"};
        PriorityQueue<Double, String> queue = new PriorityQueue<Double, String>();
        queue.insert(200.15, "собака");
        queue.insert(10.232, "человек");
        queue.insert(5.11, "пингвин");
        queue.insert(500.0, "попугай");
        Iterator<String> iter = queue.iterator();
        for (int i = 0; iter.hasNext(); i++){
            String actualRes = iter.next();
            Assert.assertEquals(expectedRes[i], actualRes);
        }
    }


    @Test (expected = NoSuchElementException.class)
    public void iteratorExceptionTest() {
        String[] expectedRes = {"попугай", "собака", "пингвин", "человек"};
        PriorityQueue<Double, String> queue = new PriorityQueue<Double, String>();
        queue.insert(200.15, "собака");
        queue.insert(10.232, "человек");
        queue.insert(5.11, "пингвин");
        queue.insert(500.0, "попугай");
        Iterator<String> iter = queue.iterator();
        for (int i = 0; iter.hasNext(); i++){
            String actualRes = iter.next();
            Assert.assertEquals(expectedRes[i], actualRes);
        }
        iter.next();
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void exceptionTest() {
        PriorityQueue<Integer, Double> queue = new PriorityQueue<Integer, Double>();
        queue.insert(200, 100500.5006);
        queue.insert(10, -2313.332);
        queue.extractmax();
        queue.extractmax();
        queue.extractmax();
    }

    @Test
    public void equalKeysTest() {
        PriorityQueue<Integer, String> queue = new PriorityQueue<Integer, String>();
        queue.insert(200, "dog");
        queue.insert(200, "human");
        queue.insert(200, "cat");
        Assert.assertEquals("dog", queue.extractmax());
        Assert.assertEquals("cat", queue.extractmax());
        Assert.assertEquals("human", queue.extractmax());
    }

    @Test
    public void streamtest() {
        PriorityQueue<Integer, String> queue = new PriorityQueue<Integer, String>();
        queue.insert(200, "dog");
        queue.insert(10, "human");
        queue.insert(5, "cat");
        assertEquals(3,queue.stream().count());
        queue.stream().forEach(x -> System.out.println(x.getValue()));
        PriorityQueue.Pair<Integer,String> act[] = (PriorityQueue.Pair<Integer, String>[]) queue.stream().toArray();
        Assert.assertEquals("dog", act[0].getValue());
        Assert.assertEquals("cat", act[1].getValue());
        Assert.assertEquals("human", act[2].getValue());
    }

}
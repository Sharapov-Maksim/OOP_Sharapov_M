import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class StackTest {

    @Test
    public void testadd() throws Exception{
        int [] expectedResults = {6,5,4,3,2,1};
        Stack<Integer> stack = new Stack<Integer>();
        stack.add(1);
        stack.add(2);
        stack.add(3);
        stack.add(4);
        stack.add(5);
        stack.add(6);
        assertEquals(6, stack.count());
        for (int i = 0; i<stack.count(); i++){
            Assert.assertEquals((Object)expectedResults[i],stack.pop());
        }
    }

    @Test
    public void testpop() throws Exception{
        int [] expectedResults = {6,4,2,1};
        Stack<Integer> stack = new Stack<Integer>();
        stack.add(1);
        stack.add(2);
        stack.add(3);
        assertEquals(3, stack.count());
        int r = stack.pop();
        assertEquals(3, r);
        stack.add(4);
        stack.add(5);
        r = stack.pop();
        assertEquals(5, r);
        stack.add(6);
        assertEquals(4, stack.count());
        for (int i = 0; i<stack.count(); i++){
            r = stack.pop();
            Assert.assertEquals(expectedResults[i],r);
        }
    }

    @Test
    public void testdouble() throws Exception{
        double [] expectedResults = {16.6,4.4,22.2};
        Stack<Double> stack = new Stack<Double>();
        stack.add(1.1);
        assertEquals((Double)1.1, stack.pop());
        stack.add(22.2);
        stack.add(3.3);
        assertEquals((Double)3.3, stack.pop());
        stack.add(4.4);
        stack.add(5.5);
        assertEquals((Double)5.5, stack.pop());
        stack.add(16.6);
        assertEquals(3, stack.count());
        for (int i = 0; i<stack.count(); i++){
            Assert.assertEquals((Double)expectedResults[i],stack.pop());
        }
    }


    @Test
    public void testIterable() throws Exception {
        int [] expectedResults = {6,555,434,3,2292,11};
        Stack<Integer> stack = new Stack<Integer>();
        stack.add(11);
        stack.add(2292);
        stack.add(3);
        stack.add(434);
        stack.add(555);
        stack.add(6);
        assertEquals(6, stack.count());
        Iterator<Integer> iter = stack.iterator();
        for (int i = 0;iter.hasNext();i++){
            int actualRes = iter.next();
            Assert.assertEquals(expectedResults[i], actualRes);
        }

    }



    @Test(expected = EmptyStackException.class)
    public void testEmptyStack() throws EmptyStackException {
        int [] expectedResults = {3,2292,11};
        Stack<Integer> stack = new Stack<Integer>();
        stack.add(11);
        stack.add(2292);
        stack.add(3);
        assertEquals(3, stack.count());
        stack.pop();
        stack.pop();
        stack.pop();
        assertEquals(0, stack.count());
        stack.pop();
    }


    @Test(expected = NoSuchElementException.class)
    public void testNoNext() throws NoSuchElementException {
        int [] expectedResults = {6,555,434,3,2292,11};
        Stack<Integer> stack = new Stack<Integer>();
        stack.add(11);
        stack.add(2292);
        stack.add(3);
        stack.add(434);
        stack.add(555);
        stack.add(6);
        assertEquals(6, stack.count());
        Iterator<Integer> iter = stack.iterator();
        for (int i = 0;iter.hasNext();i++){
            int actualRes = iter.next();
            Assert.assertEquals(expectedResults[i], actualRes);
        }
        iter.next();
    }
}
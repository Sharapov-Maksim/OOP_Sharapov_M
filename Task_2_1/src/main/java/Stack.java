import java.util.*;


/**
 * Implementation of data structure called stack
 * @param <T> T - any non-primitive Type
 */

public class Stack<T> implements Iterable<T> {
    private Object[] array;
    private int count = 0;
    private int capacity = 0;

    /**
     * Adding element to the top of stack
     * @param elem needs to have type T
     */
    public void add (T elem){
        resize();
        array[count++] = elem;
    }

    /**
     * Deleting element on the top of stack and return it
     * @return type T element, last added to stack
     * @throws NegativeArraySizeException when user try to pop empty stack
     */

    public T pop () throws Exception{
        if (count>0){
            T t;
            t = (T) array[--count];
            return t;
        }
        throw new NegativeArraySizeException("The stack is empty.\n");
    }

    /**
     * Count of elements in stack
     * @return count of elements
     */

    public int count(){
        return count;
    }

    private void resize (){
        if (count == capacity){
            Object[] newarr = new Object[capacity*2+1];
            if(array != null) System.arraycopy(array,0,newarr, 0, capacity);
            array = newarr;
            capacity = capacity*2+1;
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        Iterator<T> iter = new Iterator<T>() {
            private int currid = count-1;
            @Override
            public boolean hasNext() {
                return (currid>0);
            }

            @Override
            public T next() /*throws IndexOutOfBoundsException*/ {
                //if (!hasNext()) throw new IndexOutOfBoundsException("End of list.");\
                T result = (T)array[currid--];
                return result;
            }
        };
        return iter;
    }
}

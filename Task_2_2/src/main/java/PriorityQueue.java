import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PriorityQueue <K extends Comparable<K> ,V> implements Iterable<V> {

    // Этого наверное достаточно для StreamAPI. Да, однозначно.
    public Stream<Pair<K,V>> stream(){
        return Arrays.stream(queue);
    }



    static class Pair <K extends Comparable<K>, V> {
        K key;
        V value;
        Pair (K k, V v){
            key = k;
            value = v;
        }
        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }
    }


    private Pair<K,V>[] queue;
    //private int count = 0;
    private int capacity = 0;
    private int lstid = 0;


    private void swap (int id1, int id2){
        Pair<K,V> tmp = queue[id1];
        queue[id1] = queue[id2];
        queue[id2] = tmp;
    }


    private boolean lt (Pair<K,V> a, Pair<K,V> b){
        return a.getKey().compareTo(b.getKey()) < 0;
    }
    private boolean eq (Pair<K,V> a, Pair<K,V> b){
        return a.getKey().compareTo(b.getKey()) == 0;
    }
    private boolean gt (Pair<K,V> a, Pair<K,V> b){
        return a.getKey().compareTo(b.getKey()) > 0;
    }



    private void siftUp(int v){
        if (v != 0){
            int f = (v - 1) / 2; //parent element
            if (gt(queue[v], queue[f])) {
                swap(v, f);
                siftUp(f);
            }
        }
    }

    private void resize(){
        if (lstid==capacity){
            Pair[] newQ = new Pair[capacity*2 + 1];
            if (queue != null) System.arraycopy(queue,0, newQ, 0, lstid);
            capacity = capacity * 2 + 1;
            queue = newQ;
        }
    }

    /**
     * insert pair <key,value> to queue
     * @param key value of type K
     * @param value value of type V
     */

    public void insert (K key, V value){
        resize();
        queue[lstid++] = new Pair<>(key, value);
        siftUp(lstid-1);
    }

    private int maxID(Pair<K,V> a, Pair<K,V> b, int ida, int idb) {
        return (gt(a,b) ? ida : idb);
    }


    private void siftDown(int v){
        int ls = v * 2 + 1; //left son
        int rs = v * 2 + 2; //right son
        if (ls >= lstid) {
            return;
        }
        if ((rs == lstid) && (lt(queue[ls],queue[v]) || eq(queue[ls],queue[v]))) {
            return;
        }
        if ((rs == lstid) && lt(queue[ls], queue[v])) {
            swap(v, ls);
            return;
        }
        if ((rs < lstid) && (lt(queue[ls],queue[v]) || eq(queue[ls],queue[v])) && (lt(queue[rs],queue[v]) || eq(queue[rs],queue[v]))) {
            return;
        }
        int maximal = maxID(queue[ls], queue[rs], ls, rs);
        swap(v, maximal);
        siftDown(maximal);
    }

    /**
     * take pair <key,value> with max key and delete it from queue
     * @return value of taken pair
     */

    public V extractmax(){
        if (lstid == 0){
            throw new ArrayIndexOutOfBoundsException();
        }
        Pair<K,V> res = queue[0];
        swap(0, --lstid);
        if (lstid!=0) {
            siftDown(0);
        }
        return res.getValue();

    }



    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<V> iterator() {
        Iterator<V> iter = new Iterator<V>() {
            private int currid = 0;
            @Override
            public boolean hasNext() {
                return (currid<lstid-1);
            }

            @Override
            public V next() throws NoSuchElementException {
                if (!hasNext()) throw new NoSuchElementException("Iteration has no more elements");
                return queue[currid++].getValue();
            }
        };
        return iter;
    }
}

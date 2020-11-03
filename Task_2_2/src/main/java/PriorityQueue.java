import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PriorityQueue <K extends Comparable<K> ,V> implements Iterable<V> {
    static class Pair <K extends Comparable<K>, V> {
        private K key;
        private V value;
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

    Stream<Pair<K,V>> stream(){
        return StreamSupport.stream(new QueueSpliterator(), false);
    }

    Stream<Pair<K,V>> parallelStream(){
        return StreamSupport.stream(new QueueSpliterator(), true);
    }

    public class QueueSpliterator implements Spliterator<PriorityQueue.Pair<K,V>> {
        int curr;
        int lastElem;

        public QueueSpliterator (){
            curr = 0;
            lastElem = capacity-1;
        }

        public QueueSpliterator (int first, int lst){
            curr = first;
            lastElem = capacity;
        }
        /**
         * If a remaining element exists, performs the given action on it,
         * returning {@code true}; else returns {@code false}.  If this
         * Spliterator is {@link #ORDERED} the action is performed on the
         * next element in encounter order.  Exceptions thrown by the
         * action are relayed to the caller.
         *
         * @param action The action
         * @return {@code false} if no remaining elements existed
         * upon entry to this method, else {@code true}.
         * @throws NullPointerException if the specified action is null
         */
        @Override
        public boolean tryAdvance(Consumer<? super Pair<K, V>> action) {
            if(curr<=lastElem){
                action.accept(queue[curr]);
                curr++;
                return true;
            }
            return false;
        }

        /**
         * Performs the given action for each remaining element, sequentially in
         * the current thread, until all elements have been processed or the action
         * throws an exception.  If this Spliterator is {@link #ORDERED}, actions
         * are performed in encounter order.  Exceptions thrown by the action
         * are relayed to the caller.
         *
         * @param action The action
         * @throws NullPointerException if the specified action is null
         * @implSpec The default implementation repeatedly invokes {@link #tryAdvance} until
         * it returns {@code false}.  It should be overridden whenever possible.
         */
        @Override
        public void forEachRemaining(Consumer<? super Pair<K, V>> action) {
            for(;curr<=lastElem;curr++){
                action.accept(queue[curr]);
            }
        }

        /**
         * If this spliterator can be partitioned, returns a Spliterator
         * covering elements, that will, upon return from this method, not
         * be covered by this Spliterator.
         *
         * <p>If this Spliterator is {@link #ORDERED}, the returned Spliterator
         * must cover a strict prefix of the elements.
         *
         * <p>Unless this Spliterator covers an infinite number of elements,
         * repeated calls to {@code trySplit()} must eventually return {@code null}.
         * Upon non-null return:
         * <ul>
         * <li>the value reported for {@code estimateSize()} before splitting,
         * must, after splitting, be greater than or equal to {@code estimateSize()}
         * for this and the returned Spliterator; and</li>
         * <li>if this Spliterator is {@code SUBSIZED}, then {@code estimateSize()}
         * for this spliterator before splitting must be equal to the sum of
         * {@code estimateSize()} for this and the returned Spliterator after
         * splitting.</li>
         * </ul>
         *
         * <p>This method may return {@code null} for any reason,
         * including emptiness, inability to split after traversal has
         * commenced, data structure constraints, and efficiency
         * considerations.
         *
         * @return a {@code Spliterator} covering some portion of the
         * elements, or {@code null} if this spliterator cannot be split
         * @apiNote An ideal {@code trySplit} method efficiently (without
         * traversal) divides its elements exactly in half, allowing
         * balanced parallel computation.  Many departures from this ideal
         * remain highly effective; for example, only approximately
         * splitting an approximately balanced tree, or for a tree in
         * which leaf nodes may contain either one or two elements,
         * failing to further split these nodes.  However, large
         * deviations in balance and/or overly inefficient {@code
         * trySplit} mechanics typically result in poor parallel
         * performance.
         */
        @Override
        public Spliterator<Pair<K, V>> trySplit() {
            int middle = (lastElem - curr) / 2;
            if(middle <= 1){
                return null;
            }
            int f = curr;
            int l = curr + middle;
            curr = curr + middle +1;
            return new QueueSpliterator(f,l);
        }

        /**
         * Returns an estimate of the number of elements that would be
         * encountered by a {@link #forEachRemaining} traversal, or returns {@link
         * Long#MAX_VALUE} if infinite, unknown, or too expensive to compute.
         *
         * <p>If this Spliterator is {@link #SIZED} and has not yet been partially
         * traversed or split, or this Spliterator is {@link #SUBSIZED} and has
         * not yet been partially traversed, this estimate must be an accurate
         * count of elements that would be encountered by a complete traversal.
         * Otherwise, this estimate may be arbitrarily inaccurate, but must decrease
         * as specified across invocations of {@link #trySplit}.
         *
         * @return the estimated size, or {@code Long.MAX_VALUE} if infinite,
         * unknown, or too expensive to compute.
         * @apiNote Even an inexact estimate is often useful and inexpensive to compute.
         * For example, a sub-spliterator of an approximately balanced binary tree
         * may return a value that estimates the number of elements to be half of
         * that of its parent; if the root Spliterator does not maintain an
         * accurate count, it could estimate size to be the power of two
         * corresponding to its maximum depth.
         */
        @Override
        public long estimateSize() {
            return lastElem-curr+1;
        }

        /**
         * Convenience method that returns {@link #estimateSize()} if this
         * Spliterator is {@link #SIZED}, else {@code -1}.
         *
         * @return the exact size, if known, else {@code -1}.
         * @implSpec The default implementation returns the result of {@code estimateSize()}
         * if the Spliterator reports a characteristic of {@code SIZED}, and
         * {@code -1} otherwise.
         */
        @Override
        public long getExactSizeIfKnown() {
            return estimateSize();
        }

        /**
         * Returns a set of characteristics of this Spliterator and its
         * elements. The result is represented as ORed values from {@link
         * #ORDERED}, {@link #DISTINCT}, {@link #SORTED}, {@link #SIZED},
         * {@link #NONNULL}, {@link #IMMUTABLE}, {@link #CONCURRENT},
         * {@link #SUBSIZED}.  Repeated calls to {@code characteristics()} on
         * a given spliterator, prior to or in-between calls to {@code trySplit},
         * should always return the same result.
         *
         * <p>If a Spliterator reports an inconsistent set of
         * characteristics (either those returned from a single invocation
         * or across multiple invocations), no guarantees can be made
         * about any computation using this Spliterator.
         *
         * @return a representation of characteristics
         * @apiNote The characteristics of a given spliterator before splitting
         * may differ from the characteristics after splitting.  For specific
         * examples see the characteristic values {@link #SIZED}, {@link #SUBSIZED}
         * and {@link #CONCURRENT}.
         */
        @Override
        public int characteristics() {
            return SIZED | SUBSIZED | ORDERED | NONNULL;
        }

        /**
         * Returns {@code true} if this Spliterator's {@link
         * #characteristics} contain all of the given characteristics.
         *
         * @param characteristics the characteristics to check for
         * @return {@code true} if all the specified characteristics are present,
         * else {@code false}
         * @implSpec The default implementation returns true if the corresponding bits
         * of the given characteristics are set.
         */
        @Override
        public boolean hasCharacteristics(int characteristics) {
            return (characteristics == characteristics());
        }


    }
}

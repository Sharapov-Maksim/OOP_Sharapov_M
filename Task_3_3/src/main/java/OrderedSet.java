import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class OrderedSet<T> {
    private static class Element<T> {
        ArrayList<T> greaterElements;
        ArrayList<T> lessElements;
        Element () {
            greaterElements = new ArrayList<T>();
            lessElements = new ArrayList<T>();
        }
    }
    HashMap<T, Element<T>> set = new HashMap<T, Element<T>>();

    /**
     * Конструктор упорядоченного множества
     * @param array массив элементов, из которых должно состоять множество
     */
    public OrderedSet(@NotNull T[] array){
        if (array==null) throw new IllegalArgumentException("Null as array of elements");
        for (T e : array) set.put(e, new Element<T>());
    }

    /**
     * Добавление отношения между двумя элементами
     * @param greaterElement элемент, который должен быть больше
     * @param lessElement элемент, который должен быть меньше
     */
    public void addRelation (T greaterElement, T lessElement){
        if(greaterElement==null || lessElement==null) throw new IllegalArgumentException("Null as set element");
        if(!set.containsKey(greaterElement)||!set.containsKey(lessElement)) throw new IllegalStateException("There is no such element in set");
        if (greaterElement.equals(lessElement)) return;
        if (isGreater(lessElement, greaterElement)) throw new IllegalArgumentException("First argument already less than second argument");
        set.get(greaterElement).lessElements.add(lessElement);
        set.get(lessElement).greaterElements.add(greaterElement);
    }

    /**
     * Найти все минимальные элементы
     * @return список минимальных элементов
     */
    public ArrayList<T> findMin () {
        ArrayList<T> res = new ArrayList<T>();
        Set<T> keys = set.keySet();
        for (T k : keys) {
            if (set.get(k).lessElements.isEmpty()) res.add(k);
        }
        return res;
    }

    /**
     * Найти все максимальные элементы
     * @return список максимальных элементов
     */
    public ArrayList<T> findMax () {
        ArrayList<T> res = new ArrayList<T>();
        Set<T> keys = set.keySet();
        for (T k : keys) {
            if (set.get(k).greaterElements.isEmpty()) res.add(k);
        }
        return res;
    }

    /**
     * Проверить, является текущее упрядоченное множество решёткой
     * @return true - если множество является решёточно упорядоченным, false - иначе
     */
    boolean isLattice (){
        Set<T> elems = set.keySet();

        for (T e1 : elems){
            for (T e2 : elems){
                if (e1.equals(e2)) continue;
                ArrayList<T> greaterE1 = findAllGreater(e1);
                ArrayList<T> greaterE2 = findAllGreater(e2);
                if(greaterE1.isEmpty()) greaterE1.add(e1);
                if(greaterE2.isEmpty()) greaterE2.add(e2);
                greaterE1.retainAll(greaterE2);
                if (findMinInList(greaterE1).size()!=1) return false;
            }
        }
        return true;
    }

    private ArrayList<T> findAllGreater(T element){
        Set<T> elems = set.keySet();
        ArrayList<T> res = new ArrayList<>();
        for (T e : elems){
            if (!e.equals(element) && isGreater(e,element)){
                res.add(e);
            }
        }
        return res;
    }

    private ArrayList<T> findMinInList(ArrayList<T> elems){
        ArrayList<T> res = new ArrayList<T>();
        for(T e : elems){
            boolean flag = true;
            for (T e2 : elems){
                if (!e.equals(e2) && isGreater(e, e2)) {
                    flag = false;
                    break;
                }
            }
            if (flag) res.add(e);
        }
        return res;
    }

    /**
     * Проверить, является текущее упрядоченное множество решёткой
     * @return true - если множество является решёточно упорядоченным, false - иначе
     */
    boolean isGreater (T probablyGreater, T probablyLess) {
        if (probablyGreater==null || probablyLess == null) throw new IllegalArgumentException("Null as argument");
        if (!set.containsKey(probablyGreater) || !set.containsKey(probablyLess)) throw new IllegalStateException("No such element");
        if (probablyGreater.equals(probablyLess)) return true;              // ?
        if (set.get(probablyLess).greaterElements.isEmpty()) return false;
        for (T elem : set.get(probablyLess).greaterElements){
            if (elem.equals(probablyGreater)) return true;
        }
        for (T elem : set.get(probablyLess).greaterElements) {
            if (isGreater(probablyGreater, elem)) return true;
        }
        return false;
    }

    /**
     * Проверить, является текущее упрядоченное множество линейно упорядоченным
     * @return true - если множество является линейно упорядоченным, false - иначе
     */
    public boolean isLinear(){
        Set<T> keys = set.keySet();
        for (T k1 : keys){
            for (T k2 : keys){
                if (!isGreater(k1, k2) && !isGreater(k2,k1)) return false;
            }
        }
        return true;
    }

    /**
     * Топологическая сортировка элементов
     * @return отсортированный по возрастанию список элементов
     */
    public ArrayList<T> topSort () {
        ArrayList<T> res = new ArrayList<T>(set.size());
        HashMap<T, Element<T>> save = (HashMap<T, Element<T>>) set.clone();
        ArrayList<T> minimals;
        while (!(minimals = findMinInTopSort()).isEmpty()){
            for(T m : minimals){
                res.add(m);
                set.remove(m);
            }
        }
        if (!set.isEmpty()) {
            set = save;
            throw new IllegalStateException("Couldn`t topological sort, there is circle in set");
        }
        set = save;
        return res;
    }


    private ArrayList<T> findMinInTopSort () {
        ArrayList<T> res = new ArrayList<T>();
        Set<T> keys = set.keySet();
        for (T k : keys) {
            if (isListEmpty(set.get(k).lessElements)) res.add(k);
        }
        return res;
    }

    private boolean isListEmpty (ArrayList<T> list){
        for (T e : list){
            if(set.containsKey(e)) return false;
        }
        return true;
    }
}

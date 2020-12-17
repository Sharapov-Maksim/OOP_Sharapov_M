import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderedSetTest {

    @Test
    void topSortTest() {
        OrderedSet<Integer> orSet = new OrderedSet<>(new Integer[]{2, 3, 5, 4, 1});
        orSet.addRelation(5,4);
        orSet.addRelation(4,3);
        orSet.addRelation(3,2);
        orSet.addRelation(2,1);
        assertTrue(orSet.isLattice() && orSet.isLinear());
        Integer[] expected = {1,2,3,4,5};
        ArrayList<Integer> actual = orSet.topSort();
        for (int i = 0; i<5; i++){
            assertEquals(expected[i], actual.get(i));
        }
    }


    @Test
    void topSortTest2() {
        OrderedSet<Integer> orSet = new OrderedSet<>(new Integer[]{2, 3, 5, 4, 1});
        orSet.addRelation(4,3);
        orSet.addRelation(3,2);
        orSet.addRelation(2,1);
        assertTrue(!orSet.isLattice() && !orSet.isLinear());
        Integer[] expected = {1,5,2,3,4};
        ArrayList<Integer> actual = orSet.topSort();
        for (int i = 0; i<5; i++){
            assertEquals(expected[i], actual.get(i));
        }
        ArrayList<Integer> actual2 = orSet.topSort();
        for (int i = 0; i<5; i++){
            assertEquals(expected[i], actual2.get(i));
        }
    }

    @Test
    void isNotLatticeLinear(){
        OrderedSet<Integer> orSet = new OrderedSet<>(new Integer[]{2, 3, 5, 4});
        orSet.addRelation(5,3);
        orSet.addRelation(4,3);
        orSet.addRelation(5,2);
        orSet.addRelation(4,2);
        assertTrue(!orSet.isLattice() && !orSet.isLinear());
    }

    @Test
    void isLatticeNotLinear(){
        OrderedSet<Integer> orSet = new OrderedSet<>(new Integer[]{2, 3, 5, 4});
        orSet.addRelation(5,3);
        orSet.addRelation(4,3);
        orSet.addRelation(5,2);
        orSet.addRelation(4,2);
        orSet.addRelation(5,4);
        assertTrue(orSet.isLattice() && !orSet.isLinear());
    }
}
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Integer.max;
import static org.junit.Assert.*;

public class QuadTreeTest {

    @Test
    public void adddeleteTest() {
        QuadTree.PointObject a = new QuadTree.PointObject(1.21, 4.56, "first point");
        QuadTree.PointObject b = new QuadTree.PointObject(-0.66, -14.5, "second point");
        QuadTree.PointObject c = new QuadTree.PointObject(1.69, -5.5, "third point");
        QuadTree.PointObject d = new QuadTree.PointObject(-8.9, 7.2, "fourth point");
        QuadTree.PointObject e = new QuadTree.PointObject(1.541, 4.56, "fifth point");
        QuadTree.PointObject[] arr = {a,b,c,d};
        QuadTree qt = new QuadTree(arr);
        qt.add(e);
        qt.delete(e.x, e.y);
        qt.delete(a.x,a.y);
        assertEquals(b.o, qt.getObject(b.x, b.y));
        assertEquals(c.o, qt.getObject(c.x, c.y));
        assertEquals(d.o, qt.getObject(d.x, d.y));
    }

    @Test (expected = NoSuchElementException.class)
    public void exceptionOfGet() {
        QuadTree.PointObject a = new QuadTree.PointObject(1.21, 4.56, "first point");
        QuadTree.PointObject b = new QuadTree.PointObject(-0.66, -14.5, "second point");
        QuadTree.PointObject c = new QuadTree.PointObject(1.69, -5.5, "third point");
        QuadTree.PointObject d = new QuadTree.PointObject(-8.9, 7.2, "fourth point");
        QuadTree.PointObject e = new QuadTree.PointObject(1.541, 4.56, "fifth point");

        QuadTree.PointObject[] arr = {a,b,c,d};
        QuadTree qt = new QuadTree(arr);
        assertEquals(c.o, qt.getObject(c.x, c.y));
        qt.getObject(10500.,-322244.565);
    }

    @Test (expected = IllegalStateException.class)
    public void exceptionOfAdd() {
        QuadTree.PointObject a = new QuadTree.PointObject(1.21, 4.56, "first point");
        QuadTree.PointObject b = new QuadTree.PointObject(-0.66, -14.5, "second point");
        QuadTree.PointObject c = new QuadTree.PointObject(1.69, -5.5, "third point");
        QuadTree.PointObject d = new QuadTree.PointObject(-8.9, 7.2, "fourth point");
        QuadTree.PointObject e = new QuadTree.PointObject(1.541, 4.56, "fifth point");
        QuadTree.PointObject ban = new QuadTree.PointObject(1.541, 4.56, "baaaaaaaan");
        QuadTree.PointObject[] arr = {a,b,c,d};
        QuadTree qt = new QuadTree(arr);
        qt.add(e);
        qt.add(ban);
    }

    @Test
    public void fromRectangleTest() {
        QuadTree.PointObject a = new QuadTree.PointObject(1.21, 4.56, "first point");
        QuadTree.PointObject b = new QuadTree.PointObject(-0.66, -14.5, "second point");
        QuadTree.PointObject c = new QuadTree.PointObject(1.69, -5.5, "third point");
        QuadTree.PointObject d = new QuadTree.PointObject(-8.9, 7.2, "fourth point");
        QuadTree.PointObject e = new QuadTree.PointObject(1.541, 4.56, "fifth point");
        QuadTree.PointObject[] arr = {a,b,c,d,e};
        QuadTree qt = new QuadTree(arr);
        Object[] exp = {"fifth point", "first point", "fourth point", "second point", "third point"};
        Object[] actual = qt.getFromRectangle(-100,-100,100,100);
        for (int i = 0; i < max(exp.length, actual.length);i++) {
            assertEquals(exp[i],actual[i]);
        }
    }

    @Test
    public void iterableTest() {
        QuadTree.PointObject a = new QuadTree.PointObject(1.21, 4.56, "first point");
        QuadTree.PointObject b = new QuadTree.PointObject(-0.66, -14.5, "second point");
        QuadTree.PointObject c = new QuadTree.PointObject(1.69, -5.5, "third point");
        QuadTree.PointObject d = new QuadTree.PointObject(-8.9, 7.2, "fourth point");
        QuadTree.PointObject e = new QuadTree.PointObject(1.541, 4.56, "fifth point");
        QuadTree.PointObject[] arr = {a,b,c,d,e};
        QuadTree qt = new QuadTree(arr);
        Object[] exp = {"fifth point", "first point", "fourth point", "second point", "third point"};
        Iterator<Object> iter = qt.iterator();
        int i = 0;
        for (i = 0; iter.hasNext();i++) {
            assertEquals(exp[i],iter.next());
        }
        assertEquals(exp.length,i);
    }

    @Test
    public void streamTest() {
        QuadTree.PointObject a = new QuadTree.PointObject(1.21, 4.56, "first point");
        QuadTree.PointObject b = new QuadTree.PointObject(-0.66, -14.5, "second point");
        QuadTree.PointObject c = new QuadTree.PointObject(1.69, -5.5, "third point");
        QuadTree.PointObject d = new QuadTree.PointObject(-8.9, 7.2, "fourth point");
        QuadTree.PointObject e = new QuadTree.PointObject(1.541, 4.56, "fifth point");
        QuadTree.PointObject[] arr = {a,b,c,d,e};
        QuadTree qt = new QuadTree(arr);
        Object[] exp = {"fifth point", "first point", "fourth point", "second point", "third point"};
        assertEquals(5,qt.stream().count());
        assertArrayEquals(exp,qt.stream().toArray());
    }
}
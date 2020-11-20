import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.decrementExact;

//TODO objects with equal coords

public class QuadTree implements Iterable<Object> {


    public static class PointObject {
        double x, y;
        Object o;

        PointObject (double x, double y, Object obj){
            this.x = x;
            this.y = y;
            this.o = obj;
        }

        Object getObject (){
            return o;
        }
    }
    // axis-aligned bounding box, AABB
    static class AABB{
        double centerX, centerY;
        double halfDimension;

        AABB (double x, double y, double halfDimension){
            centerX = x;
            centerY = y;
            this.halfDimension = halfDimension;
        }

        boolean containsPoint (double x, double y){
            double p1x = centerX - halfDimension;
            double p1y = centerY - halfDimension;
            double p2x = centerX + halfDimension;
            double p2y = centerY + halfDimension;
            return x >= p1x && y >= p1y && x < p2x && y < p2y;
        }

/*
   |---------(x2,y2)
   |            |
   |            |      <-this AABB
   |            |
(x1,y1)---------|
   |-----------------------(x2,y2)
   |                          |
   |                          |   <- another Rectangle to check intersection with
   |                          |
(x1,y1)-----------------------|
 */
        boolean intersectsRectangle(double x1, double y1, double x2, double y2){
            double AABB_x1 = centerX - halfDimension;
            double AABB_y1 = centerY - halfDimension;
            double AABB_x2 = centerX + halfDimension;
            double AABB_y2 = centerY + halfDimension;

            return !(AABB_y2 < y1 || AABB_y1 > y2 || AABB_x1 > x2 || AABB_x2 < x1);

        }
    }
/* индексы в массиве поддеревьев соответствующие четвертям плоскости
            |
       1    |   0
            |
 -----------+------------
            |
       2    |   3
            |
*/
    AABB box;
    PointObject obj;
    QuadTree[] subTrees;
    QuadTree parent;

    /**
     * Конструктор для содания пустого дерева квадрантов
     * @param cX  центр дерева по оси X
     * @param cY  центр дерева по оси Y
     * @param hDim  половина размера дерева вдоль оси
     * @param parent указатель на родительскую вершину, null если создаваемая вершина корневая
     *               не рекомендуется самостоятельно вызывать этот конструктор с не null значением parent
     *               это повлечёт неопределённое поведение
     *               *вследствие этого возможно стоит сделать конструктор приватным*
     */
    private QuadTree (double cX, double cY, double hDim, QuadTree parent){
        obj = null;
        box = new AABB(cX,cY,hDim);
        subTrees = null;
        this.parent = parent;
    }

    /**
     * Создать пустое дерево квадрантов
     */
    QuadTree (){
        obj = null;
        box = new AABB(0.0,0.0,Double.MAX_VALUE);
        subTrees = null;
        this.parent = null;
    }

    /**
     * Создать дерево квадрантов по массиву объектов с их координатами
     * @param arr  массив объектов с их координатами
     */

    QuadTree (PointObject[] arr){
        obj = null;
        box = new AABB(0.0,0.0,Double.MAX_VALUE);
        subTrees = null;
        this.parent = null;

        for(PointObject obj : arr){
            this.add(obj);
        }
    }
    private void subdivide (){
        subTrees = new QuadTree[4];
        double cX, cY, hdim;
        hdim = box.halfDimension /2.;
        cX = box.centerX + hdim;
        cY = box.centerY + hdim;
        subTrees[0] = new QuadTree(cX, cY, hdim, this);
        cX = box.centerX - hdim;
        cY = box.centerY + hdim;
        subTrees[1] = new QuadTree(cX, cY, hdim, this);
        cX = box.centerX - hdim;
        cY = box.centerY - hdim;
        subTrees[2] = new QuadTree(cX, cY, hdim, this);
        cX = box.centerX + hdim;
        cY = box.centerY - hdim;
        subTrees[3] = new QuadTree(cX, cY, hdim, this);
    }

    /**
     * Добавление объекта в дерево квадрантов
     * @param object объект с координатами
     * @return true если объект был успешно добавлен, false - иначе
     */
    boolean add (PointObject object) throws IllegalStateException{
        if (!box.containsPoint(object.x, object.y)) return false;

        if (obj==null && subTrees == null) {
            obj = object;
            return true;
        }
        if(obj!=null && doub_eq(obj.x, object.x) && doub_eq(obj.y, object.y)) {
            throw new IllegalStateException("В дереве уже есть объект с такими координатами");
        }

        if (subTrees==null) subdivide();

        boolean f1 = false, f2 = false;
        if(obj!=null){
            for (int i = 0; i<4; i++){
                if (subTrees[i].add(obj)) f1 = true;
            }
        }
        for (int i = 0; i<4; i++){
            if (subTrees[i].add(object)) f2 = true;
        }
        obj = null;
        return f1 && f2;
    }

    /**
     * Удалить объект по заданным координатам
     * @param x координата по оси X
     * @param y координата по оси Y
     * @return true если удалось удалить объект, false - иначе
     */

    boolean delete (double x, double y){
        if(!box.containsPoint(x,y)) return false;
        if (obj != null){
            if(doub_eq(obj.x, x) && doub_eq(obj.y, y)){
                obj = null;
                subTrees = null;
                if(parent!=null) parent.rebalance();
                parent = null;
                return true;
            }
            else return false;
        }
        for(int i = 0; i<4; i++){
            if(subTrees[i].delete(x,y)) return true;
        }
        return false;
    }


    private void rebalance(){
        if(subTrees==null) return;
        if(obj!= null) return;
        int cnt = 0, subtree = -1;

        for(int i = 0; i<4; i++){
            if(subTrees[i].subTrees!=null) cnt+= 2;
            if(subTrees[i].obj!=null) {
                cnt+=1;
                subtree = i;
            }
        }
        if(cnt==1 && subtree!=-1){
            obj = subTrees[subtree].obj;
            subTrees = null;
        }
        if(parent!=null) parent.rebalance();
    }

    /**
     * получить объект по указанным координатам
     * @param x  координата по оси X
     * @param y  координата по оси Y
     * @return  Чистый объект, имеющий координаты x и y
     * @throws NoSuchElementException в случае, если объекта с такими координатами нет
     */

    Object getObject(double x, double y) throws NoSuchElementException{
        PointObject res = getPointObject(x,y);
        if (res!=null) return res.getObject();
        else throw new NoSuchElementException();
    }

    private PointObject getPointObject(double x, double y){
        if (!box.containsPoint(x,y)) return null;
        if (obj!=null && doub_eq(obj.x, x) && doub_eq(obj.y, y)) return obj;
        if (subTrees!=null){
            for(int i =0; i<4; i++){
                PointObject res;
                if ((res=subTrees[i].getPointObject(x,y))!=null) {
                    return res;
                }
            }
        }
        return null;
    }

    /**
     * Получить список объектов в заданном прямоугольнике
     * @return массив объектов лежащих в прямоугольнике
     * если объектов не найдено возвращается null
     */
/*
   |-----------------------(ruX,ruY)
   |                          |
   |                          |   <- Rectangle
   |                          |
(ldX,ldY)---------------------|


 */


    Object[] getFromRectangle(double ldX, double ldY, double ruX, double ruY) throws IllegalArgumentException{
        if(ldX>ruX || ldY>ruY) throw new IllegalArgumentException("Введённые координаты не корректны");
        if(!box.intersectsRectangle(ldX,ldY,ruX,ruY)) return null;
        if(obj!=null && rectangleContainsPoint(obj.x, obj.y, ldX, ldY, ruX, ruY)){
            return new Object[]{obj.getObject()};
        }
        if(subTrees!=null){
            Object[] res0, res1, res2, res3;
            int cnt =0;
            res0 = subTrees[0].getFromRectangle(ldX,ldY,ruX,ruY);
            if(res0!=null) cnt+=res0.length;
            res1 = subTrees[1].getFromRectangle(ldX,ldY,ruX,ruY);
            if(res1!=null) cnt+=res1.length;
            res2 = subTrees[2].getFromRectangle(ldX,ldY,ruX,ruY);
            if(res2!=null) cnt+=res2.length;
            res3 = subTrees[3].getFromRectangle(ldX,ldY,ruX,ruY);
            if(res3!=null) cnt+=res3.length;
            Object[] resArr = new Object[cnt];
            int l = 0;
            if(res0!=null) {
                System.arraycopy(res0, 0, resArr, l, res0.length);
                l+=res0.length;
            }
            if(res1!=null) {
                System.arraycopy(res1, 0, resArr, l, res1.length);
                l+=res1.length;
            }
            if(res2!=null) {
                System.arraycopy(res2, 0, resArr, l, res2.length);
                l+=res2.length;
            }
            if(res3!=null) {
                System.arraycopy(res3, 0, resArr, l, res3.length);
                l+=res3.length;
            }
            return resArr;
        }
        return null;
    }

    private boolean rectangleContainsPoint(double x, double y, double x1, double y1, double x2, double y2){
        return x >= x1 && x < x2 && y >= y1 && y < y2;
    }

    private boolean doub_eq (double a, double b){
        double eps = 0.000000000001;
        return (abs(a-b)<eps);
    }

    private boolean isLast(QuadTree t){
        return subTrees==null;
    }

    /**
     * Returns an iterator over elements of type Object
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<Object> iterator() {
        double x1 = box.centerX - box.halfDimension;
        double y1 = box.centerY - box.halfDimension;
        double x2 = box.centerX + box.halfDimension;
        double y2 = box.centerY + box.halfDimension;
        Object[] res = getFromRectangle(x1, y1, x2, y2);
        return Arrays.stream(res).iterator();
    }
/* stream через iterator, т.к. делал spliterator в прошлой задаче */
    Stream<Object> stream(){
        Iterator<Object> iter = iterator();
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iter,0),false);
    }
}

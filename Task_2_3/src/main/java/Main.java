import java.util.Arrays;
import java.util.Iterator;

import static java.lang.Math.sqrt;

public class Main {
    public static void main(String[] args) {
        QuadTree tree1 = Main.createCircle(3,4, 5, 2);
        QuadTree tree2 = Main.createCircle(-15,20, 150, 0.01);
        QuadTree tree3 = Main.createCircle(-100500,-44443, 2330, 0.1);
        System.out.println(tree1.getObject(-2,4));
    }

    //создаёт квадродерево на основании точек, принадлежащих заданной окружности, точки вычисляются с шагом tolerance по оси X
    static QuadTree createCircle (double xcenter, double ycenter, double r, double tolerance) throws IllegalArgumentException{
        if (tolerance>r) throw new IllegalArgumentException("Tolerance should be less than radius");
        double x = -r;
        double y = 0;
        QuadTree res = new QuadTree();
        res.add(crPointCircle(x+xcenter,y+ycenter));
        int i = 1;
        while (x<r-tolerance){
            x += tolerance;
            y =sqrt(r*r - x*x);
            res.add(crPointCircle(x+xcenter,y+ycenter));
            y *= -1;
            res.add(crPointCircle(x+xcenter,y+ycenter));
        }
        x = r;
        y = 0;
        res.add(crPointCircle(x+xcenter,y+ycenter));
        return res;
    }

    static QuadTree.PointObject crPointCircle(double x, double y){
        return new QuadTree.PointObject(x,y,"Circle");
    }
}




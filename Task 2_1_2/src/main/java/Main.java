import java.util.ArrayDeque;

public class Main {
    public static void main(String[] args) {
        Pizzeria pizzeria = new Pizzeria(5, 10,100, 10);
        pizzeria.startWork(140, "saveBakers.json", "saveDeliverymans.json");
    }
}

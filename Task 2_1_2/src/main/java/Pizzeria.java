import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Pizzeria {
    private int bakersCount;
    private int deliverymansCount;
    private QueueContainer<Order> orders;
    private QueueContainer<Pizza> storage;
    private ArrayList<Baker> bakersList;
    private ArrayList<DeliveryMan> deliverymansList;
    private boolean isPizzeriaOpened = false;
    private boolean bakersStopedWorking = false;

    public enum Menu {
        Margherita, Carbonara, Napoletana, Valtellina;
    }
    private class QueueContainer<T>{
        private ArrayDeque<T> queue;
        private int maxSize;

        QueueContainer(int size){
            this.maxSize = size;
            queue = new ArrayDeque<T>(size);
        }

        synchronized public void add(T object){
            while (queue.size() >= maxSize){
                try {
                    wait();
                } catch (InterruptedException ignored) { }
            }
            queue.add(object);
            notify();
        }
        synchronized public T get(){
            while (queue.size() < 1){
                try {
                    wait();
                } catch (InterruptedException ignored) { }
            }
            T res = queue.poll();
            notify();
            return res;
        }
        synchronized public boolean isEmpty(){
            return queue.isEmpty();
        }
    }

    private class Order{
        private final int id;
        private final int pizzaType;
        private final String pizzaName;
        Order (int id, int pizzaType, String pizzaName){
            if (id < 0 || pizzaType < 0) throw new IllegalArgumentException("Order ID and pizza type should be not negative value");
            this.id = id;
            this.pizzaType = pizzaType;
            this.pizzaName = pizzaName;
        }
    }

    private class Pizza{
        private final int typeID;
        private final String name;
        private final Order order;
        Pizza (int type, String name, Order order){
            this.typeID = type;
            this.name = name;
            this.order = order;
        }
        public int getTypeID() {
            return typeID;
        }
    }

    private class Baker extends Thread{
        private String name;
        private int workExperience;
        private final Thread t;

        Baker(int workExperience, String name){
            if (workExperience<0) throw new IllegalArgumentException("Work experience should not be negative value");
            if (name == null) throw new IllegalArgumentException("Null as argument");
            this.workExperience = workExperience;
            this.name = name;
            this.t = new Thread(this);
            t.setName(name);
            t.start();
        }

        public void run(){
            while (isPizzeriaOpened) {
                if (!orders.isEmpty()) {
                    Order order = orders.get();
                    System.out.println("Order №: " + order.id + "    State: order taken by baker " + this.name);
                    int timeToWork;
                    if (workExperience == 0)
                        timeToWork = 700;
                    timeToWork = (int) (500 + Math.round(100 * (1. / workExperience)));
                    try {
                        Thread.sleep(timeToWork);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Order №: " + order.id + "    State: pizza cooked, storing...");
                    storage.add(new Pizza(order.pizzaType, order.pizzaName, order));
                    System.out.println("Order №: " + order.id + "    State: added to storage and ready to delivery");
                }
            }
        }
    }

    private class DeliveryMan extends Thread{
        private final String name;
        private final int trunkSize;
        private ArrayList<Pizza> trunk;
        private int pizzasInTrunk = 0;
        private final Thread t;

        DeliveryMan(String name, int maxCapacity){
            if (name == null) throw new IllegalArgumentException("Name should not be Null");
            if (maxCapacity < 1) throw new IllegalArgumentException("Trunk size of deliveryman should be positive value");
            this.name = name;
            this.trunkSize = maxCapacity;
            this.trunk = new ArrayList<>(maxCapacity);
            this.t = new Thread(this);
            t.setName(name);
            t.start();
        }

        public void run(){
            while (!bakersStopedWorking || !storage.isEmpty()) {
                // Взятие доставщиком пицц со склада
                while (pizzasInTrunk < trunkSize && !storage.isEmpty()) {
                    Pizza p = storage.get();
                    trunk.add(p);
                    System.out.println("Order №: " + p.order.id + "    State: Deliveryman " + this.name + " takes pizza");
                    pizzasInTrunk++;
                }
                // Доставка каждой взятой пиццы
                for (Pizza p : trunk){
                    int timeToDelivery = (int) (400 + (new Random()).nextInt(300));
                    try {
                        Thread.sleep(timeToDelivery);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Order №: " + p.order.id + "    State: Pizza was delivered");
                }
                trunk.removeAll(trunk); // ¯\_(ツ)_/¯
                pizzasInTrunk = 0;
            }
        }

    }

    Pizzeria (int bakersCount, int deliverymansCount, int maxOrdersCnt, int storageSize){
        if (bakersCount <= 0 || deliverymansCount <= 0) throw new IllegalArgumentException("Count of workers should be positive");
        this.bakersCount = bakersCount;
        this.deliverymansCount = deliverymansCount;
        this.orders = new QueueContainer<Order>(maxOrdersCnt);
        this.storage = new QueueContainer<Pizza>(storageSize);
        this.bakersList = new ArrayList<>(bakersCount);
        this.deliverymansList = new ArrayList<>(deliverymansCount);
    }

    /**
     * Запустить самостоятельную работу пиццерии
     * @param ordersCnt количество заказов, которые будут сгенерированы и обработаны
     */
    public void startWork(int ordersCnt){
        // Открытие пиццерии и создание работников
        isPizzeriaOpened = true;
        for(int i = 0; i<bakersCount; i++){
            bakersList.add(new Baker(i,"Baker"+i));
        }
        for(int i = 0; i<deliverymansCount; i++){
            deliverymansList.add(new DeliveryMan("Deliveryman"+i, i%3 + 1));
        }
        //save("bakers.json", "deliverymans.json");
        // Генерация заказов
        for (int ID = 0; ID < ordersCnt; ID++){
            generateOrder(ID);
        }
        // Закрытие пиццерии и ожидание оканчания работы работников
        isPizzeriaOpened = false;
        for (Baker bakerThread: bakersList){
            try{
                bakerThread.t.join();
            }
            catch (InterruptedException e){
                System.out.println(bakerThread.t + " interrupted");
            }
        }
        bakersStopedWorking = true;
        for (DeliveryMan deliverymanThread: deliverymansList) {
            try {
                deliverymanThread.t.join();
            } catch (InterruptedException e) {
                System.out.println(deliverymanThread.t + " interrupted");
            }
        }
    }


    void generateOrder(int ID){
        int pizzaType = new Random().nextInt(3);
        String name = String.valueOf(Menu.values()[pizzaType]);
        Order ord = new Order(ID, pizzaType, name);
        System.out.println("Order №: " + ID + "    State: order received. Pizza name: " + name);
        orders.add(ord);
    }

    public void save (String fileNameForBakers, String fileNameForDeliverymans) {
        Gson gson = new Gson();
        String jsonBakers = gson.toJson(bakersList);
        try(FileWriter writer = new FileWriter(fileNameForBakers, false)) {
            writer.write(jsonBakers);
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        Gson gson2 = new Gson();
        String jsonDeliverymans = gson2.toJson(deliverymansList);
        try(FileWriter writer = new FileWriter(fileNameForDeliverymans, false)) {
            writer.write(jsonDeliverymans);
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}

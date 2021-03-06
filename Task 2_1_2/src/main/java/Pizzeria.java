import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Pizzeria {
    private final int bakersCount;
    private final int deliverymansCount;
    private final QueueContainer<Order> orders;
    private final QueueContainer<Pizza> storage;
    private final ArrayList<Baker> bakersList;
    private final ArrayList<DeliveryMan> deliverymansList;
    private Boolean isPizzeriaOpened = false;
    private Boolean bakersStillWorking = false;

    public enum Menu {
        Margherita, Carbonara, Napoletana, Valtellina;
    }
    private static class QueueContainer<T>{
        private final ArrayDeque<T> queue;
        private final int maxSize;

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
        synchronized public T get(Boolean exitflag){
            while (queue.size() < 1){
                try {
                    wait();
                    if (exitflag) {
                        return null;
                    }
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

    private static class Order{
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

    private static class Pizza{
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
        private final String name;
        private final int workExperience;  // Опыт работы - влияет на время приготовления пиццы
        private final Thread t;

        /**
         * Конструктор класса пекаря
         * @param workExperience Опыт работы пекаря - влияет на время, требуемое на приготовление пиццы
         * @param name           Имя пекаря
         */
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
            while (isPizzeriaOpened || !orders.isEmpty()) {
                    // Взятие заказа
                    Order order = orders.get(isPizzeriaOpened);
                    if (order == null && orders.isEmpty()) return; // В случае если поток разбудили и работать больше не надо
                    else if (order == null && !orders.isEmpty()) continue; // Если нужно продолжить работу
                    System.out.println("Order №: " + order.id + "    State: order taken by baker " + this.name);
                    // Приготовление пиццы
                    int timeToWork;
                    if (workExperience == 0)
                        timeToWork = 700;
                    timeToWork = (int) (500 + Math.round(100 * (1. / workExperience)));
                    try {
                        Thread.sleep(timeToWork);       // "приготовление пиццы"
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Order №: " + order.id + "    State: pizza cooked, storing...");
                    storage.add(new Pizza(order.pizzaType, order.pizzaName, order)); // Пицца кладётся на склад
                    System.out.println("Order №: " + order.id + "    State: added to storage and ready to delivery");
            }
        }
    }

    private class BakerSave{
        private final String name;
        private final int workExperience;  // Опыт работы - влияет на время приготовления пиццы

        /**
         * Конструктор класса пекаря
         * @param workExperience Опыт работы пекаря - влияет на время, требуемое на приготовление пиццы
         * @param name           Имя пекаря
         */
        BakerSave(int workExperience, String name){
            if (workExperience<0) throw new IllegalArgumentException("Work experience should not be negative value");
            if (name == null) throw new IllegalArgumentException("Null as argument");
            this.workExperience = workExperience;
            this.name = name;
        }
    }

    private class DeliveryMan extends Thread{
        private final String name;
        private final int trunkSize;    // Размер багажника (максимальное кол-во одновременно перевозимых пицц)
        private final ArrayList<Pizza> trunk; // Багажник
        private int pizzasInTrunk = 0;  // Счётчик кол-ва пицц в багажнике
        private final Thread t;

        /**
         * Конструктор доставщика
         * @param name        Имя доставщика пиццы
         * @param maxCapacity Размер багажника
         */
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
            while (bakersStillWorking || !storage.isEmpty()) {
                // Взятие доставщиком пицц со склада
                while (pizzasInTrunk < trunkSize && !storage.isEmpty()) {
                    Pizza p = storage.get(bakersStillWorking);
                    if (p == null) break;
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
                trunk.removeAll(trunk);
                pizzasInTrunk = 0;
            }
        }

    }

    private class DeliveryManSave{
        private final String name;
        private final int trunkSize;    // Размер багажника (максимальное кол-во одновременно перевозимых пицц)
        private final ArrayList<Pizza> trunk; // Багажник
        private int pizzasInTrunk = 0;  // Счётчик кол-ва пицц в багажнике

        /**
         * Конструктор доставщика
         * @param name        Имя доставщика пиццы
         * @param maxCapacity Размер багажника
         */
        DeliveryManSave(String name, int maxCapacity){
            if (name == null) throw new IllegalArgumentException("Name should not be Null");
            if (maxCapacity < 1) throw new IllegalArgumentException("Trunk size of deliveryman should be positive value");
            this.name = name;
            this.trunkSize = maxCapacity;
            this.trunk = new ArrayList<>(maxCapacity);
        }
    }
    /**
     * Конструктор класса пиццерии
     * @param bakersCount       Количество пекарей в пиццерии
     * @param deliverymansCount Количество доставщиков в пиццерии
     * @param maxOrdersCnt      Максимальное кол-во заказов в очереди
     * @param storageSize       Максимальное кол-во пицц хранящихся на складе
     */
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
    public void startWork(int ordersCnt, String bakersJSONFileName, String deliverymansJSONFileName){
        // Открытие пиццерии и создание работников
        isPizzeriaOpened = true;
        bakersStillWorking = true;
        loadWorkers(bakersJSONFileName, deliverymansJSONFileName);

        // Генерация заказов
        for (int ID = 0; ID < ordersCnt; ID++){
            generateOrder(ID);
        }
        saveWorkers("saveBakers.json","saveDeliverymans.json");

        // Закрытие пиццерии и ожидание оканчания работы работников
        isPizzeriaOpened = false;
        synchronized (orders) {
            orders.notifyAll();
        }
        for (Baker bakerThread: bakersList){
            try{
                bakerThread.t.join();
            }
            catch (InterruptedException e){
                System.out.println(bakerThread.t + " interrupted");
            }
        }
        bakersStillWorking = false;
        synchronized (storage){
            storage.notifyAll();
        }
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

    public void saveWorkers (String fileNameForBakers, String fileNameForDeliverymans) {
        ArrayList<BakerSave> listB = new ArrayList<BakerSave>(bakersCount);
        for (Baker baker : this.bakersList){
            listB.add(new BakerSave(baker.workExperience, baker.name));
        }
        String jsonBakers = new Gson().toJson(listB);
        try(FileWriter writer = new FileWriter(fileNameForBakers, false)) {
            writer.write(jsonBakers);
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        ArrayList<DeliveryManSave> listD = new ArrayList<DeliveryManSave>(bakersCount);
        for (DeliveryMan dm : this.deliverymansList){
            listD.add(new DeliveryManSave(dm.name, dm.trunkSize));
        }
        String jsonDeliverymans = new Gson().toJson(listD);
        try(FileWriter writer = new FileWriter(fileNameForDeliverymans, false)) {
            writer.write(jsonDeliverymans);
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void loadWorkers (String fileNameForBakers, String fileNameForDeliverymans){
        if (fileNameForBakers != null) {
            String json = null;
            try {
                json = new String(Files.readAllBytes(Paths.get(fileNameForBakers)));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type type = new TypeToken<ArrayList<BakerSave>>(){}.getType();
            ArrayList<BakerSave> res = gson.fromJson(json, type);
            ArrayList<BakerSave> listB = new ArrayList<BakerSave>(bakersCount);
            for (BakerSave baker : res) {
                this.bakersList.add(new Baker(baker.workExperience, baker.name));
            }
        }
        else{
            for(int i = 0; i<bakersCount; i++){
                bakersList.add(new Baker(i,"Baker"+i)); // Добавление пекарей
            }
        }
        if (fileNameForDeliverymans != null) {
            String json = null;
            try {
                json = new String(Files.readAllBytes(Paths.get(fileNameForDeliverymans)));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type type = new TypeToken<ArrayList<DeliveryManSave>>(){}.getType();
            ArrayList<DeliveryManSave> res = gson.fromJson(json,type);
            ArrayList<DeliveryManSave> listB = new ArrayList<DeliveryManSave>(bakersCount);
            for (DeliveryManSave dms : res) {
                this.deliverymansList.add(new DeliveryMan(dms.name, dms.trunkSize));
            }
        }
        else{
            for(int i = 0; i<deliverymansCount; i++){
                deliverymansList.add(new DeliveryMan("Deliveryman"+i, 3)); // Добавление доставщиков
            }
        }
    }
}

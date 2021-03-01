import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Predicate<Integer> pred = Main::isNotPrime;

        ArrayList<Integer> numbers = new ArrayList<Integer>();
        File file = new File("primeNumbersOnly.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String text;
            while ((text = reader.readLine()) != null) {
                numbers.add(Integer.parseInt(text));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean flag = false;
        System.out.println("Поиск с помощью последовательной программы...");
        long start1 = System.nanoTime();
        for (int elem : numbers){
            if (isNotPrime(elem)) {
                System.out.println("Найдено не простое число: " + elem);
                flag = true;
            }
        }
        long duration1 = (System.nanoTime() - start1) / 1_000_000;
        if (!flag) System.out.println("Нет не простых чисел");
        System.out.println("Длительность последовательной программы: " + duration1 + "\n");

        WithThreads<Integer> threadsHandler = new WithThreads<Integer>();
        ArrayList<Integer> threadsCnts = new ArrayList<>(5);
        threadsCnts.add(1);
        threadsCnts.add(2);
        threadsCnts.add(4);
        threadsCnts.add(8);
        threadsCnts.add(16);

        for (int N : threadsCnts){
            System.out.println("Поиск... Количество используемых потоков: " + N);
            long start = System.nanoTime();
            WithThreads.Answer<Integer> answer = threadsHandler.runThreads(numbers,N,pred);
            long duration = (System.nanoTime() - start) / 1_000_000;
            if (answer.isAnswerFound())
                System.out.println("Найдено не простое число: " + answer.getAnswer());
            else System.out.println("Нет не простых чисел");
            System.out.println("Время, мс: " + duration);
        }


        System.out.println("Поиск с помощью parallelStream...");
        Stream<Integer> parStream = numbers.parallelStream();
        boolean result = parStream.anyMatch(pred);
        if (result) System.out.println("Не простое число найдено");
        else System.out.println("Нет не простых чисел");

    }

    public static boolean isNotPrime(int number){
        for(int i = 2; i*i<number; i++){
            if (number % i == 0)
                return true;
        }
        return false;
    }




}

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class WithThreadsTest {

    @Test
    void notEnoughTasks() {
        ArrayList<Integer> numbers = new ArrayList<>(5);
        numbers.add(17);
        numbers.add(19);
        numbers.add(37);
        numbers.add(83);
        numbers.add(96);
        int N = 8;
        Predicate<Integer> pred = Main::isNotPrime;
        WithThreads<Integer> threadsHandler = new WithThreads<Integer>();

        System.out.println("Поиск... \n" + "Количество чисел: " + numbers.size()+ "\nКоличество используемых потоков: " + N);
        long start = System.nanoTime();
        WithThreads.Answer<Integer> answer = threadsHandler.runThreads(numbers,N,pred);
        long duration = (System.nanoTime() - start) / 1_000_000;
        if (answer.isAnswerFound())
            System.out.println("Найдено не простое число: " + answer.getAnswer());
        else System.out.println("Нет не простых чисел");
        System.out.println("Время, мс: " + duration + "\n===========================\n");
        assertEquals(96,answer.getAnswer());
    }

    @Test
    void doNotExtraWork() {
        ArrayList<Integer> numbers = new ArrayList<>(10);
        numbers.add(1306550237);
        numbers.add(702287413);
        numbers.add(34);
        numbers.add(290564033);
        numbers.add(602191487);
        numbers.add(1306550237);
        numbers.add(702287413);
        numbers.add(15);
        numbers.add(4);
        numbers.add(25);
        int N = 2;
        Predicate<Integer> pred = Main::isNotPrime;
        WithThreads<Integer> threadsHandler = new WithThreads<Integer>();

        System.out.println("Поиск... \n" + "Количество чисел: " + numbers.size()+ "\nКоличество используемых потоков: " + N);
        long start = System.nanoTime();
        WithThreads.Answer<Integer> answer = threadsHandler.runThreads(numbers,N,pred);
        long duration = (System.nanoTime() - start) / 1_000_000;
        if (answer.isAnswerFound())
            System.out.println("Найдено не простое число: " + answer.getAnswer());
        else System.out.println("Нет не простых чисел");
        System.out.println("Время, мс: " + duration + "\n===========================\n");
        assertEquals(34,answer.getAnswer());
    }

    @Test
    void largeOneNotPrime() {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        File file = new File("numbersWithOneNotPrime.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String text;
            while ((text = reader.readLine()) != null) {
                numbers.add(Integer.parseInt(text));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int N = 8;
        Predicate<Integer> pred = Main::isNotPrime;
        WithThreads<Integer> threadsHandler = new WithThreads<Integer>();

        System.out.println("Поиск... \n" + "Количество чисел: " + numbers.size()+ "\nКоличество используемых потоков: " + N);
        long start = System.nanoTime();
        WithThreads.Answer<Integer> answer = threadsHandler.runThreads(numbers,N,pred);
        long duration = (System.nanoTime() - start) / 1_000_000;
        if (answer.isAnswerFound())
            System.out.println("Найдено не простое число: " + answer.getAnswer());
        else System.out.println("Нет не простых чисел");
        System.out.println("Время, мс: " + duration + "\n===========================\n");
        assertEquals(145,answer.getAnswer());
    }

    @Test
    void largeOnlyPrime() {
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
        int N = 8;
        Predicate<Integer> pred = Main::isNotPrime;
        WithThreads<Integer> threadsHandler = new WithThreads<Integer>();

        System.out.println("Поиск... \n" + "Количество чисел: " + numbers.size()+ "\nКоличество используемых потоков: " + N);
        long start = System.nanoTime();
        WithThreads.Answer<Integer> answer = threadsHandler.runThreads(numbers,N,pred);
        long duration = (System.nanoTime() - start) / 1_000_000;
        if (answer.isAnswerFound())
            System.out.println("Найдено не простое число: " + answer.getAnswer());
        else System.out.println("Нет не простых чисел");
        System.out.println("Время, мс: " + duration + "\n===========================\n");
        assertFalse(answer.isAnswerFound());
    }

}
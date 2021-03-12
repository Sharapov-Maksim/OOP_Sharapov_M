import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

public class WithThreads<T> {

    private ArrayList<T> tasks;
    private Answer<T> answer;

    public static class Answer<T> {
        private boolean isFound = false;
        private T answ;

        Answer(){}

        synchronized void setAnswer(T answer){
            this.answ = answer;
            this.isFound = true;
        }

        boolean isAnswerFound(){
            return isFound;
        }

        T getAnswer (){
            return answ;
        }

    }

    public static class ThreadHandler<T> extends Thread{
        private final Iterator<T> iterator;
        private final Predicate<T> predicate;
        private Answer<T> answer;
        private final Thread t;

        ThreadHandler(Iterator<T> iter, Predicate<T> pred, Answer<T> answ){
            this.iterator  = iter;
            this.predicate = pred;
            this.answer    = answ;
            this.t = new Thread(this);
            t.start();
        }

        public void run(){
            while (iterator.hasNext()){
                if (answer.isAnswerFound()) return;
                T elem = iterator.next();
                if(predicate.test(elem)){
                    //System.out.println("Найдено не простое число");
                    answer.setAnswer(elem);
                }
            }
        }



    }

    WithThreads(){
        answer = new Answer<T>();
    }

    /**
     * Выполнение поиска непростого числа с созданием множества потоков
     * @param tasksArray ArrayList с числами для поиска
     * @param threadsCnt количество потоков, которое нужно создать для решения задачи
     * @param predicate  предикат для поиска элемента, удовлетворяющего ему
     * @return объект класса Answer<T> для возврата ответа
     */
    Answer<T> runThreads (ArrayList<T> tasksArray, int threadsCnt, Predicate<T> predicate) {
        if (tasksArray==null || predicate==null) throw new IllegalArgumentException("Null as argument");
        boolean isNotEnoughElements = false;
        ArrayList<ThreadHandler<T>> handlers = new ArrayList<>();
        for(int i = 0; i<threadsCnt; i++){
            Iterator<T> iter = tasksArray.iterator();
            for (int j = 0; j<i;j++) {
                if (iter.hasNext())
                    iter.next();
                else {
                    isNotEnoughElements = true;
                    break;
                }
            }
            if (isNotEnoughElements) break;
            Iterator<T> task = subDivide(iter,threadsCnt);
            handlers.add(new ThreadHandler<T>(task, predicate, answer));
        }
        for (ThreadHandler<T> handler : handlers){
            try{
                handler.t.join();
            }
            catch (InterruptedException e){
                System.out.println(handler.t + " interrupted");
            }
        }
        return answer;
    }

    private Iterator<T> subDivide (@NotNull Iterator<T> iter, int N){
        ArrayList<T> everyNth = new ArrayList<T>();
        for (int i = 0;iter.hasNext(); i++) {
            T elem = iter.next();
            if (i % N == 0){
                everyNth.add(elem);
            }
        }
        return everyNth.iterator();
    }

}

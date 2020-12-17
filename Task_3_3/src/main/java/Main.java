import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        OrderedSet<String> set = new OrderedSet<>(new String[] {"Мария", "Василий", "Татьяна", "Дмитрий", "Вероника"});
        set.addRelation("Мария", "Василий");
        set.addRelation("Вероника", "Василий");
        set.addRelation("Василий", "Татьяна");
        ArrayList<String> res = set.findMax();
        for (String s : res){
            System.out.println(s);
        }
    }
}

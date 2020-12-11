import java.util.Stack;

public class Cos implements ICommand{
    @Override
    public String getTokenRepresentation() {
        return "cos";
    }

    @Override
    public void apply(Stack<Double> args) {
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для исполнения функции");
        args.push(Math.cos(args.pop()));
    }
}

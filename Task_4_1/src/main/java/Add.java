import java.util.Stack;

public class Add implements ICommand {
    @Override
    public String getTokenRepresentation() {
        return "+";
    }
    @Override
    public void apply(Stack<Double> args) {
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для выполнения операции");
        double a = args.pop();
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для выполнения операции");
        double b = args.pop();
        args.push(a+b);
    }
}

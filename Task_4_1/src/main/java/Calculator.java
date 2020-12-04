import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Интерфейс для функций и операций
 */
interface ICommand {
    String getTokenRepresentation();
    void apply(Stack<Double> args);
}

class SinCommand implements ICommand{
    @Override
    public String getTokenRepresentation() {
        return "sin";
    }

    @Override
    public void apply(Stack<Double> args) {
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для исполнения функции");
        args.push(Math.sin(args.pop()));
    }
}
class CosCommand implements ICommand{
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
class SqrtCommand implements ICommand{
    @Override
    public String getTokenRepresentation() {
        return "sqrt";
    }

    @Override
    public void apply(Stack<Double> args) {
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для исполнения функции");
        args.push(Math.sqrt(args.pop()));
    }
}
class LogCommand implements ICommand{
    @Override
    public String getTokenRepresentation() {
        return "log";
    }

    @Override
    public void apply(Stack<Double> args) {
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для исполнения функции");
        args.push(Math.log(args.pop()));
    }
}
class PowCommand implements ICommand{
    @Override
    public String getTokenRepresentation() {
        return "pow";
    }

    @Override
    public void apply(Stack<Double> args) {
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для исполнения функции");
        double x = args.pop();
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для исполнения функции");
        double y = args.pop();
        args.push(Math.pow(x,y));
    }
}
class PlusCommand implements ICommand{
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
class MinusCommand implements ICommand{
    @Override
    public String getTokenRepresentation() {
        return "-";
    }

    @Override
    public void apply(Stack<Double> args) {
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для выполнения операции");
        double a = args.pop();
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для выполнения операции");
        double b = args.pop();
        args.push(a-b);
    }
}
class MultCommand implements ICommand{
    @Override
    public String getTokenRepresentation() {
        return "*";
    }

    @Override
    public void apply(Stack<Double> args) {
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для выполнения операции");
        double a = args.pop();
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для выполнения операции");
        double b = args.pop();
        args.push(a*b);
    }
}
class DivCommand implements ICommand{
    @Override
    public String getTokenRepresentation() {
        return "/";
    }

    @Override
    public void apply(Stack<Double> args) {
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для выполнения операции");
        double a = args.pop();
        if (args.isEmpty()) throw new IllegalStateException("Недостаточно аргументов для выполнения операции");
        double b = args.pop();
        args.push(a/b);
    }
}


public class Calculator {
    HashMap<String, ICommand> handlers;
    Stack<Double> numbers;
    Stack<String> tokens;
    private String[] expr;

    /**
     * Конструктор калькулятора
     * @param inStream входной поток данных, оттуда считывается 1 строка
     */
    Calculator(InputStream inStream) {
        if(inStream==null) throw new IllegalArgumentException("null в качестве потока ввода/вывода");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inStream))){
            expr = in.readLine().split(" ");
            tokens = new Stack<String>();
            tokens.addAll(Arrays.asList(expr));
            numbers = new Stack<Double>();
            handlers = new HashMap<String, ICommand>();
            loadCommands();
        } catch (IOException e){
            System.out.println("Произошла ошибка ввода");
        }
    }

    /**
     * Конструктор калькулятора
     * @param expression выражение для вычисления, все токены должны быть разделены пробелами ' '
     */
    Calculator(String expression){
        if(expression==null) throw new IllegalArgumentException("null в качестве выражения");
        expr = expression.split(" ");
        tokens = new Stack<String>();
        tokens.addAll(Arrays.asList(expr));
        numbers = new Stack<Double>();
        handlers = new HashMap<String, ICommand>();
        loadCommands();
    }


    private void registerCommand(ICommand handler){
        if (handler == null) throw new NullPointerException("Переданн null в качестве команды");
        this.handlers.put(handler.getTokenRepresentation(), handler);
    }
    private void loadCommands(){
        registerCommand(new SinCommand());
        registerCommand(new CosCommand());
        registerCommand(new PlusCommand());
        registerCommand(new MinusCommand());
        registerCommand(new DivCommand());
        registerCommand(new MultCommand());
        registerCommand(new SqrtCommand());
        registerCommand(new LogCommand());
        registerCommand(new PowCommand());
    }

    public void loadCommand(String name){
        registerCommand(loadConstructorOfICommand(name));
    }

    /**
     * Добавление плагина-функции
     * @param className имя класса, реализующего интерфейс ICommand
     */
    private ICommand loadConstructorOfICommand (String className){
        try {
            ClassLoader cl = Calculator.class.getClassLoader();
            Class cls = cl.loadClass(className);
            Constructor<?> ctor = cls.getConstructor();
            Object object = ctor.newInstance(new Object[] {});
            return (ICommand) object;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Не удалось загрузить плагин", e);
        }
    }

    /**
     * Посчитать выражение, переданное при создании экземпляра класса или последнего вызова newExpr
     * @return посчитанное значение
     */
    public double parseExpression(){
        if(tokens.isEmpty()) throw new IllegalArgumentException("Пустая строка");
        String token;
        //double res;
        while (!tokens.isEmpty()){
            token = tokens.pop();
            if (isNumeric(token)){
                numbers.push(Double.parseDouble(token));
            }
            else if (handlers.containsKey(token)){
                handlers.get(token).apply(numbers);
            }
            else throw new UnsupportedOperationException("Данная функция не поддерживается или неверный формат выражения");
        }
        if (numbers.size()!=1) throw new IllegalArgumentException("Неверный формат выражения");
        double res = numbers.pop();
        // "Перезагрузка" калькулятора, чтобы можно было получить значение выражения несколько раз
        tokens = new Stack<String>();
        tokens.addAll(Arrays.asList(expr));
        handlers = new HashMap<String, ICommand>();
        loadCommands();
        //
        return res;
    }

    /**
     * Поменять хранимое выражение для вычисления
     * @param expression строка, содержащая новое выражение
     */
    public void newExpr(String expression){
        if(expression==null) throw new IllegalArgumentException("null в качестве выражения");
        expr = expression.split(" ");
        tokens = new Stack<String>();
        tokens.addAll(Arrays.asList(expr));
        numbers = new Stack<Double>();
        handlers = new HashMap<String, ICommand>();
        loadCommands();
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isOperation(String token){
        return (token.equals("+") || token.equals("-") || token.equals("/") || token.equals("*"));
    }
}

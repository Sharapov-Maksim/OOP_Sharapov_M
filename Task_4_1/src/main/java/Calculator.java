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
 * ��������� ��� ������� � ��������
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
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� �������");
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
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� �������");
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
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� �������");
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
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� �������");
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
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� �������");
        double x = args.pop();
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� �������");
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
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� ��������");
        double a = args.pop();
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� ��������");
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
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� ��������");
        double a = args.pop();
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� ��������");
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
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� ��������");
        double a = args.pop();
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� ��������");
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
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� ��������");
        double a = args.pop();
        if (args.isEmpty()) throw new IllegalStateException("������������ ���������� ��� ���������� ��������");
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
     * ����������� ������������
     * @param inStream ������� ����� ������, ������ ����������� 1 ������
     */
    Calculator(InputStream inStream) {
        if(inStream==null) throw new IllegalArgumentException("null � �������� ������ �����/������");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inStream))){
            expr = in.readLine().split(" ");
            tokens = new Stack<String>();
            tokens.addAll(Arrays.asList(expr));
            numbers = new Stack<Double>();
            handlers = new HashMap<String, ICommand>();
            loadCommands();
        } catch (IOException e){
            System.out.println("��������� ������ �����");
        }
    }

    /**
     * ����������� ������������
     * @param expression ��������� ��� ����������, ��� ������ ������ ���� ��������� ��������� ' '
     */
    Calculator(String expression){
        if(expression==null) throw new IllegalArgumentException("null � �������� ���������");
        expr = expression.split(" ");
        tokens = new Stack<String>();
        tokens.addAll(Arrays.asList(expr));
        numbers = new Stack<Double>();
        handlers = new HashMap<String, ICommand>();
        loadCommands();
    }


    private void registerCommand(ICommand handler){
        if (handler == null) throw new NullPointerException("�������� null � �������� �������");
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
     * ���������� �������-�������
     * @param className ��� ������, ������������ ��������� ICommand
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
            throw new IllegalArgumentException("�� ������� ��������� ������", e);
        }
    }

    /**
     * ��������� ���������, ���������� ��� �������� ���������� ������ ��� ���������� ������ newExpr
     * @return ����������� ��������
     */
    public double parseExpression(){
        if(tokens.isEmpty()) throw new IllegalArgumentException("������ ������");
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
            else throw new UnsupportedOperationException("������ ������� �� �������������� ��� �������� ������ ���������");
        }
        if (numbers.size()!=1) throw new IllegalArgumentException("�������� ������ ���������");
        double res = numbers.pop();
        // "������������" ������������, ����� ����� ���� �������� �������� ��������� ��������� ���
        tokens = new Stack<String>();
        tokens.addAll(Arrays.asList(expr));
        handlers = new HashMap<String, ICommand>();
        loadCommands();
        //
        return res;
    }

    /**
     * �������� �������� ��������� ��� ����������
     * @param expression ������, ���������� ����� ���������
     */
    public void newExpr(String expression){
        if(expression==null) throw new IllegalArgumentException("null � �������� ���������");
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

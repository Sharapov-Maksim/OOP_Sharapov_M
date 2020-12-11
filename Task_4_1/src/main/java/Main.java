import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;

public class Main {
    public static void main(String[] args) {
        Calculator calc = new Calculator(System.in);
        calc.loadCommand("Add");
        calc.loadCommand("Cos");
        System.out.println(calc.parseExpression());
    }
}

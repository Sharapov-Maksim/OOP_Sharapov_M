import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    public void calculatorTestTrigonometry() {
        String expr = "sin + - 1 2 1";
        Calculator calc = new Calculator(expr);
        Assertions.assertEquals(0,calc.parseExpression(),0.00000001);
        String expr2 = "cos 0";
        Calculator calc2 = new Calculator(expr2);
        Assertions.assertEquals(1,calc2.parseExpression(),0.00000001);
        String expr3 = "cos * 2 4";
        Calculator calc3 = new Calculator(expr3);
        Assertions.assertEquals(Math.cos(8),calc3.parseExpression(),0.00000001);

    }

    @Test
    public void calculatorTest2() {
        String expr = "pow 2 10";
        Calculator calc = new Calculator(expr);
        Assertions.assertEquals(1024,calc.parseExpression(),0.00000001);
        String expr2 = "log / 343 44";
        calc.newExpr(expr2);
        Assertions.assertEquals(Math.log(343/44.),calc.parseExpression(),0.00000001);
        String expr3 = "log - 151 232";
        calc.newExpr(expr3);
        assertTrue(Double.isNaN(calc.parseExpression()));
    }

    @Test
    public void calculatorTest3() {
        String expr = "/ pow 2 10 4";
        Calculator calc = new Calculator(expr);
        Assertions.assertEquals(1024/4.,calc.parseExpression(),0.00000001);
        String expr2 = "log / 343 * 44 2";
        calc.newExpr(expr2);
        Assertions.assertEquals(Math.log(343/88.),calc.parseExpression(),0.00000001);
        String expr3 = "log + * 3 - 2 NaN 232";
        calc.newExpr(expr3);
        assertTrue(Double.isNaN(calc.parseExpression()));
    }

    @Test
    public void calculatorTest4() {
        String expr = "sqrt / pow 2 10 4";
        Calculator calc = new Calculator(expr);
        Assertions.assertEquals(16,calc.parseExpression(),0.00000001);
        String expr2 = "log + sqrt 1443.3434 / 343 * 44 2";
        calc.newExpr(expr2);
        Assertions.assertEquals(Math.log(343/88.+Math.sqrt(1443.3434)),calc.parseExpression(),0.00000001);
    }

    @Test
    public void calculatorTestExc1() {
        String expr = "ln 2.7";
        Calculator calc = new Calculator(expr);
        Assertions.assertThrows(UnsupportedOperationException.class, calc::parseExpression);
    }
    @Test
    public void calculatorTestExc2() {
        String expr = "787 2.7";
        Calculator calc = new Calculator(expr);
        Assertions.assertThrows(IllegalArgumentException.class, calc::parseExpression);
    }

}
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static org.junit.Assert.*;

public class RecordBookTest {
    @Test
    public void testMyBook (){
        ArrayList<RecordBook.Rec> sem1 = new ArrayList<RecordBook.Rec>();
        sem1.add(new RecordBook.Rec("Введение в алгебру и анализ", 5));
        sem1.add(new RecordBook.Rec("Декларативное программирование", 5));
        sem1.add(new RecordBook.Rec("Императивное программирование", 5));
        sem1.add(new RecordBook.Rec("История", 5));
        sem1.add(new RecordBook.Rec("Основы культуры речи", 5));
        sem1.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", 5));
        ArrayList<RecordBook.Rec> sem2 = new ArrayList<RecordBook.Rec>();
        sem2.add(new RecordBook.Rec("Введение в алгебру и анализ", 5));
        sem2.add(new RecordBook.Rec("Декларативное программирование", 5));
        sem2.add(new RecordBook.Rec("Императивное программирование", 5));
        sem2.add(new RecordBook.Rec("Иностранный язык", 5));
        sem2.add(new RecordBook.Rec("Цифровые платформы", 5));
        sem2.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", 5));
        RecordBook book = new RecordBook(3);
        book.replaceSemester(1,sem1);
        book.replaceSemester(2,sem2);
        doub_eq_assert(5, book.avg());
        Assert.assertTrue(book.increasedScholarship());
        Assert.assertTrue(book.isRedDiplomaPossible());
    }

    @Test
    public void testBookNotIncreased (){
        ArrayList<RecordBook.Rec> sem1 = new ArrayList<RecordBook.Rec>();
        sem1.add(new RecordBook.Rec("Введение в алгебру и анализ", 4));
        sem1.add(new RecordBook.Rec("Декларативное программирование", 5));
        sem1.add(new RecordBook.Rec("Императивное программирование", 5));
        sem1.add(new RecordBook.Rec("История", 4));
        sem1.add(new RecordBook.Rec("Основы культуры речи", 5));
        sem1.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", 4));
        ArrayList<RecordBook.Rec> sem2 = new ArrayList<RecordBook.Rec>();
        sem2.add(new RecordBook.Rec("Введение в алгебру и анализ", 5));
        sem2.add(new RecordBook.Rec("Декларативное программирование", 5));
        sem2.add(new RecordBook.Rec("Императивное программирование", 5));
        sem2.add(new RecordBook.Rec("Иностранный язык", 4));
        sem2.add(new RecordBook.Rec("Цифровые платформы", 5));
        sem2.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", 5));
        RecordBook book = new RecordBook(3);
        book.replaceSemester(1,sem1);
        book.replaceSemester(2,sem2);
        doub_eq_assert(1.*(4+5+5+4+5+4+5+5+5+4+5+5)/12, book.avg());
        //book.addRecordToSemester(1,new RecordBook.Rec("Баннный предмет",3));
        Assert.assertFalse(book.increasedScholarship());
        Assert.assertTrue(book.isRedDiplomaPossible());
    }

    @Test
    public void testBookNoRedDipl (){
        ArrayList<RecordBook.Rec> sem1 = new ArrayList<RecordBook.Rec>();
        sem1.add(new RecordBook.Rec("Введение в алгебру и анализ", 4));
        sem1.add(new RecordBook.Rec("Декларативное программирование", 5));
        sem1.add(new RecordBook.Rec("Императивное программирование", 5));
        sem1.add(new RecordBook.Rec("История", 4));
        sem1.add(new RecordBook.Rec("Основы культуры речи", 3));
        sem1.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", 4));
        ArrayList<RecordBook.Rec> sem2 = new ArrayList<RecordBook.Rec>();
        sem2.add(new RecordBook.Rec("Введение в алгебру и анализ", 4));
        sem2.add(new RecordBook.Rec("Декларативное программирование", 5));
        sem2.add(new RecordBook.Rec("Императивное программирование", 5));
        sem2.add(new RecordBook.Rec("Иностранный язык", 4));
        sem2.add(new RecordBook.Rec("Цифровые платформы", 5));
        sem2.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", 5));
        RecordBook book = new RecordBook(3);
        book.replaceSemester(1,sem1);
        book.replaceSemester(2,sem2);
        doub_eq_assert(1.*(4+5+5+4+3+4+4+5+5+4+5+5)/12, book.avg());
        //book.addRecordToSemester(1,new RecordBook.Rec("Баннный предмет",3));
        Assert.assertFalse(book.increasedScholarship());
        Assert.assertFalse(book.isRedDiplomaPossible());
    }

    @Test (expected = IllegalStateException.class)
    public void testException1 (){
        ArrayList<RecordBook.Rec> sem1 = new ArrayList<RecordBook.Rec>();
        sem1.add(new RecordBook.Rec("Введение в алгебру и анализ", 4));
        sem1.add(new RecordBook.Rec("Декларативное программирование", 5));
        sem1.add(new RecordBook.Rec("Императивное программирование", 5));
        sem1.add(new RecordBook.Rec("История", 4));
        sem1.add(new RecordBook.Rec("Основы культуры речи", 3));
        sem1.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", 4));
        RecordBook book = new RecordBook(5);
        book.replaceSemester(1,sem1);
        book.avg();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testException2 (){
        RecordBook book = new RecordBook(-2);
    }

    @Test (expected = IllegalStateException.class)
    public void testException3 (){
        ArrayList<RecordBook.Rec> sem1 = new ArrayList<RecordBook.Rec>();
        sem1.add(new RecordBook.Rec("Введение в алгебру и анализ", 4));
        sem1.add(new RecordBook.Rec("Декларативное программирование", 5));
        sem1.add(new RecordBook.Rec("Императивное программирование", 5));
        sem1.add(new RecordBook.Rec("История", 4));
        sem1.add(new RecordBook.Rec("Основы культуры речи", 3));
        sem1.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", 4));
        RecordBook book = new RecordBook(5);
        book.replaceSemester(1,sem1);
        book.increasedScholarship();
    }

    private void doub_eq_assert (double a, double b){
        double eps = 0.000000000001;
        Assert.assertTrue(abs(a-b)<eps);
    }
}


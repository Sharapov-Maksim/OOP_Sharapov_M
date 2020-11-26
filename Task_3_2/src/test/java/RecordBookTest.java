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
        RecordBook.Grade G;
        ArrayList<RecordBook.Rec> sem1 = new ArrayList<RecordBook.Rec>();
        sem1.add(new RecordBook.Rec("Введение в алгебру и анализ", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("Декларативное программирование", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("Императивное программирование", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("История", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("Основы культуры речи", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", RecordBook.Grade.FIVE));
        ArrayList<RecordBook.Rec> sem2 = new ArrayList<RecordBook.Rec>();
        sem2.add(new RecordBook.Rec("Введение в алгебру и анализ", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Декларативное программирование", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Императивное программирование", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Иностранный язык", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Цифровые платформы", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", RecordBook.Grade.FIVE));
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
        sem1.add(new RecordBook.Rec("Введение в алгебру и анализ", RecordBook.Grade.FOUR));
        sem1.add(new RecordBook.Rec("Декларативное программирование", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("Императивное программирование", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("История", RecordBook.Grade.FOUR));
        sem1.add(new RecordBook.Rec("Основы культуры речи", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", RecordBook.Grade.FOUR));
        ArrayList<RecordBook.Rec> sem2 = new ArrayList<RecordBook.Rec>();
        sem2.add(new RecordBook.Rec("Введение в алгебру и анализ", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Декларативное программирование", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Императивное программирование", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Иностранный язык", RecordBook.Grade.FOUR));
        sem2.add(new RecordBook.Rec("Цифровые платформы", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", RecordBook.Grade.FIVE));
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
        sem1.add(new RecordBook.Rec("Введение в алгебру и анализ", RecordBook.Grade.FOUR));
        sem1.add(new RecordBook.Rec("Декларативное программирование", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("Императивное программирование", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("История", RecordBook.Grade.FOUR));
        sem1.add(new RecordBook.Rec("Основы культуры речи", RecordBook.Grade.THREE));
        sem1.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", RecordBook.Grade.FOUR));
        ArrayList<RecordBook.Rec> sem2 = new ArrayList<RecordBook.Rec>();
        sem2.add(new RecordBook.Rec("Введение в алгебру и анализ", RecordBook.Grade.FOUR));
        sem2.add(new RecordBook.Rec("Декларативное программирование", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Императивное программирование", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Иностранный язык", RecordBook.Grade.FOUR));
        sem2.add(new RecordBook.Rec("Цифровые платформы", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", RecordBook.Grade.FIVE));
        RecordBook book = new RecordBook(3);
        book.replaceSemester(1,sem1);
        book.replaceSemester(2,sem2);
        doub_eq_assert(1.*(4+5+5+4+3+4+4+5+5+4+5+5)/12, book.avg());
        //book.addRecordToSemester(1,new RecordBook.Rec("Баннный предмет",3));
        Assert.assertFalse(book.increasedScholarship());
        Assert.assertFalse(book.isRedDiplomaPossible());
    }

    @Test
    public void testAddToSem (){
        ArrayList<RecordBook.Rec> sem1 = new ArrayList<RecordBook.Rec>();
        sem1.add(new RecordBook.Rec("Введение в алгебру и анализ", RecordBook.Grade.FOUR));
        sem1.add(new RecordBook.Rec("Декларативное программирование", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("Императивное программирование", RecordBook.Grade.FIVE));
        sem1.add(new RecordBook.Rec("История", RecordBook.Grade.FOUR));
        sem1.add(new RecordBook.Rec("Основы культуры речи", RecordBook.Grade.THREE));
        sem1.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", RecordBook.Grade.FOUR));
        ArrayList<RecordBook.Rec> sem2 = new ArrayList<RecordBook.Rec>();
        sem2.add(new RecordBook.Rec("Введение в алгебру и анализ", RecordBook.Grade.FOUR));
        sem2.add(new RecordBook.Rec("Декларативное программирование", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Императивное программирование", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Иностранный язык", RecordBook.Grade.FOUR));
        sem2.add(new RecordBook.Rec("Цифровые платформы", RecordBook.Grade.FIVE));
        sem2.add(new RecordBook.Rec("Введение в дискретную математику и математическую логику", RecordBook.Grade.FIVE));
        RecordBook book = new RecordBook(4);
        book.replaceSemester(1,sem1);
        book.replaceSemester(2,sem2);
        doub_eq_assert(1.*(4+5+5+4+3+4+4+5+5+4+5+5)/12, book.avg());
        book.addRecordToSemester(3,new RecordBook.Rec("Модели вычислений", RecordBook.Grade.THREE));
        book.addRecordToSemester(3,new RecordBook.Rec("ДУ и ТФКП0", RecordBook.Grade.FOUR));
        doub_eq_assert(1.*(4+5+5+4+3+4+4+5+5+4+5+5+3+4)/14, book.avg());
        book.addRecordToSemester(3,new RecordBook.Rec("ТВ и МС", RecordBook.Grade.TWO));
        book.addRecordToSemester(3,new RecordBook.Rec("Операционные системы", RecordBook.Grade.THREE));
        book.addRecordToSemester(3,new RecordBook.Rec("Введение в ИИ", RecordBook.Grade.FIVE));
        doub_eq_assert(1.*(4+5+5+4+3+4+4+5+5+4+5+5+3+4+2+3+5)/17, book.avg());

    }

    @Test //(expected = IllegalArgumentException.class)
    public void testException2 (){
        RecordBook book = new RecordBook(-2);
    }

    private void doub_eq_assert (double a, double b){
        double eps = 0.000000000001;
        Assert.assertTrue(abs(a-b)<eps);
    }
}


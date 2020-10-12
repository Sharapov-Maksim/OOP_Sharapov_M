import jdk.jshell.execution.Util;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class SearchTest {

    @Test
    public void substrSearchInFile() throws IOException {
        // test 1
        String fname1 = "test1.txt"; // put your file name here
        String substrUTF_8 = new String("Мама".getBytes(), "UTF-8");
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname1), StandardCharsets.UTF_8));
        String strUTF_8 = new String("Мама мыла раму".getBytes(), "UTF-8");
        out.write(strUTF_8);
        if (out != null) {
            out.close();
        }
        ArrayList<Long> actualRes1 = Main.substrSearchInFile(fname1, substrUTF_8);
        ArrayList<Long> expectedRes1 = new ArrayList<Long>();
        expectedRes1.add((long)0);
        assertEquals(expectedRes1.size(),actualRes1.size());
        for (int i = 0; i < actualRes1.size(); i++){
            assertEquals(expectedRes1.get(i), actualRes1.get(i));
        }


        // test 2

        String fname2 = "test2.txt"; // put your file name here
        substrUTF_8 = new String("you".getBytes(), "UTF-8");
        BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname2), StandardCharsets.UTF_8));
        String strr = "Never gonna give you up\n" +
                "Never gonna let you down\n" +
                "Never gonna run around and desert you\n" +
                "Never gonna make you cry\n" +
                "Never gonna say goodbye\n" +
                "Never gonna tell a lie and hurt you";
        strUTF_8 = new String(strr.getBytes(), "UTF-8");
        out2.write(strUTF_8);

        if (out2 != null) {
            out2.close();
        }

        ArrayList<Long> actualRes2 = Main.substrSearchInFile(fname2, substrUTF_8);
        ArrayList<Long> expectedRes2 = new ArrayList<Long>();
        expectedRes2.add((long)17);
        expectedRes2.add((long)40);
        expectedRes2.add((long)83);
        expectedRes2.add((long)104);
        expectedRes2.add((long)168);
        assertEquals(expectedRes2.size(),actualRes2.size());
        for (int i = 0; i < actualRes2.size(); i++){
            assertEquals(expectedRes2.get(i), actualRes2.get(i));
        }

        //test 3
        String fname3 = "test3.txt"; // put your file name here
        substrUTF_8 = new String("Повседневная".getBytes(), "UTF-8");
        BufferedWriter out3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname3), StandardCharsets.UTF_8));
        String str3 = "Случайно сгенерированный текст:\n" +
                "Повседневная практика показывает, что постоянный количественный рост и сфера нашей активности влечет " +"" +
                "за собой процесс внедрения и модернизации новых предложений. Разнообразный и богатый опыт начало " +
                "повседневной работы по формированию позиции играет важную роль в формировании форм воздействия? " +
                "Повседневная практика показывает, что сложившаяся структура организации в значительной степени " +
                "обуславливает создание всесторонне сбалансированных нововведений!\n" +
                "\n" +
                "Повседневная практика показывает, что новая модель организационной деятельности представляет собой " +
                "интересный эксперимент проверки направлений прогрессивного развития. Повседневная практика показывает, " +
                "что постоянный количественный рост и сфера нашей активности требует определения и уточнения системы " +
                "масштабного изменения ряда параметров. Значимость этих проблем настолько очевидна, что дальнейшее " +
                "развитие различных форм деятельности играет важную роль в формировании соответствующих условий " +
                "активизации! Равным образом начало повседневной работы по формированию позиции способствует " +
                "подготовке и реализации системы обучения кадров, соответствующей насущным потребностям.\n" +
                "\n" +
                "Равным образом постоянное информационно-техническое обеспечение нашей деятельности требует от нас " +
                "системного анализа дальнейших направлений развитая системы массового участия? Задача организации, в " +
                "особенности же выбранный нами инновационный путь напрямую зависит от ключевых компонентов планируемого " +
                "обновления? Равным образом выбранный нами инновационный путь способствует подготовке и реализации форм " +
                "воздействия? Повседневная практика показывает, что дальнейшее развитие различных форм деятельности " +
                "требует от нас анализа дальнейших направлений развитая системы массового участия.\n" +
                "\n" +
                "Не следует, однако, забывать о том, что повышение уровня гражданского...";
        strUTF_8 = new String(str3.getBytes(), "UTF-8");
        out3.write(strUTF_8);

        if (out3 != null) {
            out3.close();
        }

        ArrayList<Long> actualRes3 = Main.substrSearchInFile(fname3, substrUTF_8);
        ArrayList<Long> expectedRes3 = new ArrayList<Long>();
        expectedRes3.add((long)32);
        expectedRes3.add((long)326);
        expectedRes3.add((long)488);
        expectedRes3.add((long)656);
        expectedRes3.add((long)1581);
        assertEquals(expectedRes3.size(),actualRes3.size());
        for (int i = 0; i < actualRes3.size(); i++){
            assertEquals(expectedRes3.get(i), actualRes3.get(i));
        }

        // test 4
        String fname4 = "test4.txt"; // put your file name here
        substrUTF_8 = new String("Монада - это моноид в категории эндофункторов".getBytes(), "UTF-8");
        BufferedWriter out4 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname4), StandardCharsets.UTF_8));
        strUTF_8 = new String("Монада - это моноид в категории эндофункторов".getBytes(), "UTF-8");
        out4.write(strUTF_8);
        if (out4 != null) {
            out4.close();
        }
        ArrayList<Long> actualRes4 = Main.substrSearchInFile(fname4, substrUTF_8);
        ArrayList<Long> expectedRes4 = new ArrayList<Long>();
        expectedRes4.add((long)0);
        assertEquals(expectedRes4.size(),actualRes4.size());
        for (int i = 0; i < actualRes4.size(); i++){
            assertEquals(expectedRes4.get(i), actualRes4.get(i));
        }


        //test 5
        String fname5 = "test5.txt"; // put your file name here
        substrUTF_8 = new String("пирог".getBytes(), "UTF-8");
        BufferedWriter out5 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname5), StandardCharsets.UTF_8));
        strUTF_8 = new String("Я хочу сок!".getBytes(), "UTF-8");
        out5.write(strUTF_8);
        if (out5 != null) {
            out5.close();
        }
        ArrayList<Long> actualRes5 = Main.substrSearchInFile(fname5, substrUTF_8);
        ArrayList<Long> expectedRes5 = new ArrayList<Long>();
        assertEquals(expectedRes5.size(),actualRes5.size());
        for (int i = 0; i < actualRes5.size(); i++){
            assertEquals(expectedRes5.get(i), actualRes5.get(i));
        }
    }

}
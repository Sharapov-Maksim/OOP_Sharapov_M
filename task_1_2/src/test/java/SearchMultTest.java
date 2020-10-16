import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class SearchMultTest {

    @Test // Несколько вхождений по всему тексту
    public void substrSearchInStream() throws IOException {
        // test 2
        String fname2 = "test2.txt"; // put your file name here
        String substrUTF_8 = new String("you".getBytes(), "UTF-8");
        String strr = "Never gonna give you up\n" +
                "Never gonna let you down\n" +
                "Never gonna run around and desert you\n" +
                "Never gonna make you cry\n" +
                "Never gonna say goodbye\n" +
                "Never gonna tell a lie and hurt you";
        String strUTF_8 = new String(strr.getBytes(), "UTF-8");
        File f2 = File.createTempFile("test2", ".txt");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f2), StandardCharsets.UTF_8));) {
            out.write(strUTF_8);
        }
        ArrayList<Long> actualRes2;
        try (InputStream inStream = new FileInputStream(f2)) {
            actualRes2 = Main.substrSearchInStream(inStream, substrUTF_8);
        }
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

        //test 3 // Несколько вхождений по всему тексту, но текст побольше =)
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
        File f3 = File.createTempFile("test3", ".txt");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f3), StandardCharsets.UTF_8));) {
            out.write(strUTF_8);
        }
        ArrayList<Long> actualRes3;
        try (InputStream inStream = new FileInputStream(f3)) {
            actualRes3 = Main.substrSearchInStream(inStream, substrUTF_8);
        }
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
    }
}
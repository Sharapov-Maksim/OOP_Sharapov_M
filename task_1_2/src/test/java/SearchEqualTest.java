import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class SearchEqualTest {

    @Test //Строка совпадает с искомой подстрокой
    public void substrSearchInStream() throws IOException {
        // test 4
        String fname4 = "test4.txt"; // put your file name here
        String substrUTF_8 = new String("Монада - это моноид в категории эндофункторов".getBytes(), "UTF-8");
        String strUTF_8 = new String("Монада - это моноид в категории эндофункторов".getBytes(), "UTF-8");
        File f4 = File.createTempFile("test4", ".txt");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f4), StandardCharsets.UTF_8));) {
            out.write(strUTF_8);
        }
        ArrayList<Long> actualRes4;
        try (InputStream inStream = new FileInputStream(f4)) {
            actualRes4 = Main.substrSearchInStream(inStream, substrUTF_8);
        }
        ArrayList<Long> expectedRes4 = new ArrayList<Long>();
        expectedRes4.add((long)0);
        assertEquals(expectedRes4.size(),actualRes4.size());
        for (int i = 0; i < actualRes4.size(); i++){
            assertEquals(expectedRes4.get(i), actualRes4.get(i));
        }

    }
}
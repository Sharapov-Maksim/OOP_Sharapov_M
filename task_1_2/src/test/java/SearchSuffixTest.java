import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class SearchSuffixTest {

    @Test // Подстрока является суффиксом строки
    public void substrSearchInStream() throws IOException {
        // test 6
        String substrUTF_8 = new String("abc".getBytes(), "UTF-8");
        String strUTF_8 = new String("abcaabc".getBytes(), "UTF-8");
        File f6 = File.createTempFile("test6", ".txt");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f6), StandardCharsets.UTF_8));) {
            out.write(strUTF_8);
        }
        ArrayList<Long> actualRes6;
        try (InputStream inStream = new FileInputStream(f6)) {
            actualRes6 = Main.substrSearchInStream(inStream, substrUTF_8);
        }
        ArrayList<Long> expectedRes6 = new ArrayList<Long>();
        expectedRes6.add((long) 0);
        expectedRes6.add((long) 4);
        assertEquals(expectedRes6.size(), actualRes6.size());
        for (int i = 0; i < actualRes6.size(); i++) {
            assertEquals(expectedRes6.get(i), actualRes6.get(i));
        }

        // test 7
        substrUTF_8 = new String("abc".getBytes(), "UTF-8");
        strUTF_8 = new String("abcabcaabc".getBytes(), "UTF-8");
        File f7 = File.createTempFile("test7", ".txt");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f7), StandardCharsets.UTF_8));) {
            out.write(strUTF_8);
        }
        ArrayList<Long> actualRes7;
        try (InputStream inStream = new FileInputStream(f7)) {
            actualRes7 = Main.substrSearchInStream(inStream, substrUTF_8);
        }
        ArrayList<Long> expectedRes7 = new ArrayList<Long>();
        expectedRes7.add((long) 0);
        expectedRes7.add((long) 3);
        expectedRes7.add((long) 7);
        assertEquals(expectedRes7.size(), actualRes7.size());
        for (int i = 0; i < actualRes7.size(); i++) {
            assertEquals(expectedRes7.get(i), actualRes7.get(i));
        }


    }
}
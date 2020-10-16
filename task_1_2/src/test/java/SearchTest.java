import jdk.jshell.execution.Util;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class SearchTest {

    @Test // Подстрока совпадает с началом строки
    public void tstEqualsBeg() throws IOException {
        // test 1
        String substrUTF_8 = new String("Мама".getBytes(), "UTF-8");
        String strUTF_8 = new String("Мама мыла раму".getBytes(), "UTF-8");
        File f1 = File.createTempFile("test1", ".txt");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f1), StandardCharsets.UTF_8));) {
            out.write(strUTF_8);
        }
        ArrayList<Long> actualRes1;
        try (InputStream inStream = new FileInputStream(f1)) {
            actualRes1 = Main.substrSearchInStream(inStream, substrUTF_8);
        }
        ArrayList<Long> expectedRes1 = new ArrayList<Long>();
        expectedRes1.add((long) 0);
        assertEquals(expectedRes1.size(), actualRes1.size());
        for (int i = 0; i < actualRes1.size(); i++) {
            assertEquals(expectedRes1.get(i), actualRes1.get(i));
        }
    }


    @Test // Подстрока совпадает с концом строки
    public void tstEqualsEnd() throws IOException {
        // test 8
        String substrUTF_8 = new String("мммм".getBytes(), "UTF-8");
        String strUTF_8 = new String("Мама мыла мммм".getBytes(), "UTF-8");
        File f8 = File.createTempFile("test8", ".txt");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f8), StandardCharsets.UTF_8));) {
            out.write(strUTF_8);
        }
        ArrayList<Long> actualRes8;
        try (InputStream inStream = new FileInputStream(f8)) {
            actualRes8 = Main.substrSearchInStream(inStream, substrUTF_8);
        }
        ArrayList<Long> expectedRes8 = new ArrayList<Long>();
        expectedRes8.add((long) 10);
        assertEquals(expectedRes8.size(), actualRes8.size());
        for (int i = 0; i < actualRes8.size(); i++) {
            assertEquals(expectedRes8.get(i), actualRes8.get(i));
        }
    }

}
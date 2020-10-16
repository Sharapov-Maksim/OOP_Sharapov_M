import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class Search0Test {

    @Test
    public void substrSearchInStream() throws IOException {
        //test 5
        String substrUTF_8 = new String("пирог".getBytes(), "UTF-8");
        String strUTF_8 = new String("Я хочу сок!".getBytes(), "UTF-8");
        File f5 = File.createTempFile("test5", ".txt");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f5), StandardCharsets.UTF_8));) {
            out.write(strUTF_8);
        }
        ArrayList<Long> actualRes5;
        try (InputStream inStream = new FileInputStream(f5)) {
            actualRes5 = Main.substrSearchInStream(inStream, substrUTF_8);
        }
        ArrayList<Long> expectedRes5 = new ArrayList<Long>();
        assertEquals(expectedRes5.size(),actualRes5.size());
        for (int i = 0; i < actualRes5.size(); i++){
            assertEquals(expectedRes5.get(i), actualRes5.get(i));
        }

    }
}
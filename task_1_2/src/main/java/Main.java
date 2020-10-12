import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class Main {
    public static void main (String args[]) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        String fname;
        String substrIn;
        fname = br.readLine();
        substrIn = br.readLine();
        ArrayList<Long> res = new ArrayList<>();
        res = substrSearchInFile(fname, substrIn);
        for (int i = 0; i<res.size(); i++){
            System.out.println(res.get(i));
        }
    }

    /**
     * method that search substring in specified file
     * @param fname full file name
     * @param substrIn substring that searched for
     * @return ArrayList with all entrуes of substrIn in file
     */

    public static ArrayList<Long> substrSearchInFile (String fname, String substrIn) {
        ArrayList<Long> resList = new ArrayList<>();
        char substr[] = new char[substrIn.length()];
        substr = substrIn.toCharArray();
        int lenSubStr = substr.length;
        BufferedReader readerq = null;
        FileInputStream reader = null;
        long pos = 0; //position of start substring entry
        //TODO два буфера, каждый размером в подстроку, конкат, и поиск подстроки в новой
        try {
            readerq = new BufferedReader(new InputStreamReader(new FileInputStream(fname), StandardCharsets.UTF_8));
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fname), "UTF-8"));
            char[] strForSearch = new char[lenSubStr * 2];
            char buffer1[] = new char[lenSubStr];
            char buffer2[] = new char[lenSubStr];
            int l1 = in.read(buffer1, 0, lenSubStr);
            if (l1 < lenSubStr) {
                return resList;
            }
            int l2 = in.read(buffer2, 0, lenSubStr);
            if ((l2 == -1) && (Arrays.equals(buffer1, substr))) { // when substring equals to string for search
                //System.out.println(0);
                resList.add(0L);
                return resList;
            }
            while (l2 != -1) {
                strForSearch = bufcat(buffer1, buffer2);

                /*Implemented z-function algorithm to find all substrings in string strForSearch
                * works for linear time O(N+M)*/

                char[] str = new char[lenSubStr * 3];
                str = bufcat(substr, strForSearch);
                int zArr[] = new int[str.length];
                zArr[0] = 0;
                int n = str.length;
                int l = 0, r = 0;
                for (int i = 1; i < n; ++i) {
                    if (i <= r) {
                        zArr[i] = Math.min(r - i + 1, zArr[i - l]);
                    }
                    while (i + zArr[i] < n && (str[zArr[i]] == str[i + zArr[i]])) {
                        ++zArr[i];
                    }
                    if (i + zArr[i] - 1 > r) {
                        l = i;
                        r = i + zArr[i] - 1;
                    }
                }
                for (int i = lenSubStr; i < n; i++) {
                    if (zArr[i] >= lenSubStr && !(resList.contains(pos))) {
                        resList.add(pos);
                    }
                    pos++;
                }

                l1 = l2;
                pos -= l2;
                for (int i = 0; i < lenSubStr; i++) {
                    buffer1[i] = buffer2[i];
                }
                l2 = in.read(buffer2, 0, lenSubStr);
            }
        } catch (IOException e) {
            System.out.println("IO Error: " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    System.out.println(e1);
                }
            }
        }
        return resList;
    }

        private static char[] bufcat (char[] b1, char[] b2){
        char[] res = new char[b1.length+b2.length];
        int l1 = b1.length;
        int l2 = b2.length;
        for(int i = 0; i<l1; i++){
            res[i] = b1[i];
        }
        for(int i = 0; i<l2; i++){
            res[l1+i] = b2[i];
        }
        return res;
    }
}


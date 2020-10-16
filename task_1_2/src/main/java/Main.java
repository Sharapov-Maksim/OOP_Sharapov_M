import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class Main {
    public static void main (String args[]) throws IOException{
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            String fname;
            String substrIn;
            fname = br.readLine();
            substrIn = br.readLine();
            ArrayList<Long> res = new ArrayList<>();
            try (InputStream inStream = new FileInputStream(fname)) {
                res = substrSearchInStream(inStream, substrIn);
            }
            for (int i = 0; i < res.size(); i++) {
                System.out.println(res.get(i));
            }
        }
    }

    /**
     * method that search substring in specified file
     * @param inStream input stream
     * @param substrIn substring that searched for
     * @return ArrayList with all entrуes of substrIn in file
     */

    public static ArrayList<Long> substrSearchInStream (InputStream inStream, String substrIn) throws IOException  {
        ArrayList<Long> resList = new ArrayList<>();
        char substr[] = new char[substrIn.length()];
        substr = substrIn.toCharArray();
        int lenSubStr = substr.length;
        FileInputStream reader = null;
        long pos = 0; //position of start substring entry
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "UTF-8"))){
            char str[] = new char[lenSubStr * 3];
            System.arraycopy(substr, 0, str, 0, lenSubStr);
            int l1 = in.read(str, lenSubStr, lenSubStr);
            if (l1 < lenSubStr) {
                return resList;
            }
            int l2 = in.read(str, lenSubStr*2, lenSubStr);
            if ((l2 == -1) && (Arrays.equals(str,lenSubStr, lenSubStr*2,substr, 0, lenSubStr))) { // when substring equals to string for search
                resList.add(0L);
                return resList;
            }
            while (l2 != -1) {
                /*Implemented z-function algorithm to find all substrings in string strForSearch
                * works for linear time O(N+M)*/

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
                System.arraycopy(str, lenSubStr*2, str, lenSubStr, lenSubStr);
                l2 = in.read(str, lenSubStr*2, lenSubStr);
                if (l2 < lenSubStr){
                    str[lenSubStr*2 + l2] = 0;
                }
            }
        }
        return resList;
    }
}


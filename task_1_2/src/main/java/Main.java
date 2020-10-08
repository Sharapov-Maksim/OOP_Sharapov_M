import java.io.*;

public class Main {
    public static void main (String args[]) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String fname;
        String substr;
        fname = br.readLine();
        substr = br.readLine();
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fname)));
            String strForSearch;
            strForSearch = reader.readLine();
            int lenSubStr = substr.length();
            /*Implementing z-function algorithm to find all substrings in string strForSearch*/
            String str = substr + strForSearch;
            int zArr[] = new int[str.length()];
            zArr[0] = 0;
            int n = str.length();
            int l = 0, r = 0;
            for (int i = 1; i < n; ++i) {
                if (i <= r) {
                    zArr[i] = Math.min(r - i + 1, zArr[i - l]);
                }
                while (i + zArr[i] < n && (str.charAt(zArr[i]) == str.charAt(i + zArr[i]))) {
                    ++zArr[i];
                }
                if (i + zArr[i] - 1 > r) {
                    l = i; r = i + zArr[i] - 1;
                }
            }
            int resArr[] = new int[strForSearch.length()];
            int cnt = 0;
            for (int i = lenSubStr; i < n; i++) {
                if (zArr[i] >= lenSubStr) {
                    resArr[cnt++] = i - lenSubStr;
                    System.out.println(i - lenSubStr);
                }
            }
        }catch (IOException e){
            System.out.println("IO Error: "+e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    System.out.println(e1);
                }
            }
        }

    }
}


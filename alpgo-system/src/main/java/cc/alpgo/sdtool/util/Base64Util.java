package cc.alpgo.sdtool.util;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {
    public static String refineCodeForPython(String data) throws UnsupportedEncodingException {
        int missingPadding = 4 - data.length() % 4;
        if (missingPadding != 0) {
            for (int i = 0; i < missingPadding; i++) {
                data += "=";
            }
        }
        return data;
    }
}

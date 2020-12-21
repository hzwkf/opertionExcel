package patten;

import java.util.regex.Pattern;

public class JudgmentType {
/**
 *判断一个字符串是否是数字类型
 */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?\\d+(\\.\\d*)?|\\.\\d+$");
        return pattern.matcher(str).matches();
    }

}

package com.boredream.bga.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isContainChinese(String str) {
        if (isEmpty(str)) return false;
        Pattern p = Pattern.compile("[u4e00-u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static String getDefaultStr(String str) {
        return isEmpty(str) ? "未知" : str;
    }

    public static String getMoney(float cost) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return "￥" + decimalFormat.format(cost);
    }

    public static String position2hms(long position) {
        int hours = (int) (position / DateUtils.ONE_HOUR_MILLIONS);
        int minute = (int) (position % DateUtils.ONE_HOUR_MILLIONS / DateUtils.ONE_MINUTE_MILLIONS);
        int second = (int) (position % DateUtils.ONE_MINUTE_MILLIONS / DateUtils.ONE_SECOND_MILLIONS);
        StringBuilder sb = new StringBuilder();
        if (hours < 10) {
            sb.append('0');
        }
        sb.append(hours).append(':');

        if (minute < 10) {
            sb.append('0');
        }
        sb.append(minute).append(':');

        if (second < 10) {
            sb.append('0');
        }
        sb.append(second);

        return sb.toString();
    }
}

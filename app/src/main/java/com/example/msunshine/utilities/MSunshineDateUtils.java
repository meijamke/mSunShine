package com.example.msunshine.utilities;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MSunshineDateUtils {

    public static boolean isValidDate(String date) {
//        date=date.trim();
        String format = "(20[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";
//        + "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(date);
        if (matcher.matches()) {
            pattern = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");
            matcher = pattern.matcher(date);
            if (matcher.matches()) {
                int y = Integer.valueOf(matcher.group(1));
                int m = Integer.valueOf(matcher.group(2));
                int d = Integer.valueOf(matcher.group(3));
                if (d > 28) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m - 1, 1);
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return (lastDay >= d);
                }
            }
            return true;
        }
        return false;
    }

    public static String getNormalizedDayNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public static int getNormalizedHourNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH", Locale.getDefault());
        return Integer.parseInt(dateFormat.format(Calendar.getInstance().getTime()));
    }
}

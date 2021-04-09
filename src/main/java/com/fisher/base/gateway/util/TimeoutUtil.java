package com.fisher.base.gateway.util;


import java.util.Calendar;
import java.util.Date;

/**
 * Created by fisher
 */
public class TimeoutUtil {
    public static long timeout() {
        Calendar calendar = Calendar.getInstance();
        long start = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE,14);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long diff = (calendar.getTimeInMillis() - start) / 1000;
        return diff;
    }

    /**
     * 判断传入的时间是否是“今天”，防止数据消息延迟，
     *
     *
     * @param time
     * @return
     */
    public static boolean isToday(String time) {
        if (time == null || "".equals(time.trim())) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long today = calendar.getTimeInMillis();
        Date date = new Date(Long.parseLong(time));
        return date.getTime() >= today;
    }
    /**
     * 获得当天在一年中是第几天
     * @return
     */
    public static int dayOfYear() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        return day;
    }
}

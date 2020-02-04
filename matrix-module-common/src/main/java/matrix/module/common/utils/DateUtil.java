package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wangcheng
 * @effect 处理日期
 */
public class DateUtil {

    /**
     * @return 返回当前时间   Date类型
     * @author wangcheng
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * @return 返回当前时间所在周的星期一的时间   Date类型
     * @throws ParseException
     * @author wangcheng
     */
    public static Date getFirstDayAtWhenWeek(TimeFormat format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format.getValue());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date date;
        try {
            date = sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            throw new ServiceException(e);
        }
        return date;
    }

    /**
     * @return 返回当前时间所在周的星期日的时间   Date类型
     * @throws ParseException
     * @author wangcheng
     */
    public static Date getLastDayAtWhenWeek(TimeFormat format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        return getDate(format, calendar);
    }

    /**
     * @return 返回当前时间之前一个月的时间   Date类型
     * @throws ParseException
     * @author wangcheng
     */
    public static Date getBeforeOneMonth(TimeFormat format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getNowDate());
        calendar.add(Calendar.MONTH, -1);
        return getDate(format, calendar);
    }

    /**
     * @return 返回当前时间之后一个月的时间   Date类型
     * @throws ParseException
     * @author wangcheng
     */
    public static Date getAfterOneMonth(TimeFormat format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getNowDate());
        calendar.add(Calendar.MONTH, 1);
        return getDate(format, calendar);
    }

    /**
     * @return 根据时间和pattern格式化时间格式   String类型
     * @author wangcheng
     */
    public static String formatDateStrByPattern(Date date, TimeFormat format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format.getValue());
        return sdf.format(date);
    }

    /**
     * @return 将Date格式转化成 String类型
     * @author wangcheng
     */
    public static String formatDateToStr(Date date) {
        return date.toString();
    }

    /**
     * @return 将String格式转化成 Date类型
     * @throws ParseException
     * @author wangcheng
     */
    public static Date formatStrToDate(String date, TimeFormat format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format.getValue());
        Date result;
        try {
            result = sdf.parse(date);
        } catch (ParseException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    /**
     * 获取时间
     */
    private static Date getDate(TimeFormat format, Calendar calendar) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format.getValue());
            date = sdf.parse(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            throw new ServiceException(e);
        }
        return date;
    }

    public enum TimeFormat {
        ShortTime("yyyyMMdd"),
        ShortTimeLine("yyyy-MM-dd"),
        ShortTimeUnderLine("yyyy_MM_dd"),
        ShortTimeSpritLine("yyyy/MM/dd"),
        LongTime("yyyyMMddHHmmss"),
        LongTimeStandard("yyyy-MM-dd HH:mm:ss"),
        CustomFormat("");

        private String value;

        TimeFormat(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public TimeFormat customValue(String value) {
            if ("".equals(this.value)) {
                this.value = value;
            }
            return this;
        }
    }

    /**
     * 几天前的时间
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getBeforeDay(Date date, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -day);
        return calendar.getTime();
    }

    /**
     * 几天后的时间
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getAfterDay(Date date, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 几周前的时间
     *
     * @param date
     * @param week
     * @return
     */
    public static Date getBeforeWeek(Date date, Integer week) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -week * 7);
        return calendar.getTime();
    }

    /**
     * 几周后的时间
     *
     * @param date
     * @param week
     * @return
     */
    public static Date getAfterWeek(Date date, Integer week) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, week * 7);
        return calendar.getTime();
    }

    /**
     * 几月前的时间
     *
     * @param date
     * @param month
     * @return
     */
    public static Date getBeforeMonth(Date date, Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -month);
        return calendar.getTime();
    }

    /**
     * 几月后的时间
     *
     * @param date
     * @param month
     * @return
     */
    public static Date getAfterMonth(Date date, Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 几年前的时间
     *
     * @param date
     * @param year
     * @return
     */
    public static Date getBeforeYear(Date date, Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -year);
        return calendar.getTime();
    }

    /**
     * 几年后的时间
     *
     * @param date
     * @param year
     * @return
     */
    public static Date getAfterYear(Date date, Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

}

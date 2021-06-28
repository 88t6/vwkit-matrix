package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wangcheng
 * effect 处理日期
 */
public class DateUtil {

    /**
     * @return 返回当前时间   Date类型
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * @param format 参数
     * @return 返回当前时间所在周的星期一的时间   Date类型
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
     * @param format 参数
     * @return 返回当前时间所在周的星期日的时间   Date类型
     */
    public static Date getLastDayAtWhenWeek(TimeFormat format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        return getDate(format, calendar);
    }

    /**
     * @param format 参数
     * @return 返回当前时间之前一个月的时间   Date类型
     */
    public static Date getBeforeOneMonth(TimeFormat format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getNowDate());
        calendar.add(Calendar.MONTH, -1);
        return getDate(format, calendar);
    }

    /**
     * @param format 参数
     * @return 返回当前时间之后一个月的时间   Date类型
     */
    public static Date getAfterOneMonth(TimeFormat format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getNowDate());
        calendar.add(Calendar.MONTH, 1);
        return getDate(format, calendar);
    }

    /**
     * @param date 参数
     * @param format 参数
     * @return 根据时间和pattern格式化时间格式   String类型
     */
    public static String formatDateStrByPattern(Date date, TimeFormat format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format.getValue());
        return sdf.format(date);
    }

    /**
     * @param date 参数
     * @return 将Date格式转化成 String类型
     */
    public static String formatDateToStr(Date date) {
        return date.toString();
    }

    /**
     * @param date 参数
     * @param format 参数
     * @return 将String格式转化成 Date类型
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
            if (this == TimeFormat.CustomFormat) {
                this.value = value;
            }
            return this;
        }
    }

    /**
     * 几天前的时间
     *
     * @param date 参数
     * @param day 参数
     * @return Date
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
     * @param date 参数
     * @param day 参数
     * @return Date
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
     * @param date 参数
     * @param week 参数
     * @return Date
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
     * @param date 参数
     * @param week 参数
     * @return Date
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
     * @param date 参数
     * @param month 参数
     * @return Date
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
     * @param date 参数
     * @param month 参数
     * @return Date
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
     * @param date 参数
     * @param year 参数
     * @return Date
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
     * @param date 参数
     * @param year 参数
     * @return Date
     */
    public static Date getAfterYear(Date date, Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 校验时间返回校验成功的时间
     *
     * @param startTime 参数
     * @param endTime 参数
     * @param defaultLimitDay 参数
     * @param maxLimitDay 参数
     * @param timeFormat 参数
     * @return Date[]
     */
    public static Date[] verifyDate(String startTime, String endTime,
                                    Integer defaultLimitDay, Integer maxLimitDay, DateUtil.TimeFormat timeFormat) {
        Date startDate;
        Date endDate;
        if (endTime == null || "".equals(endTime)) {
            endDate = DateUtil.getNowDate();
        } else {
            endDate = DateUtil.formatStrToDate(endTime, timeFormat);
        }
        if (startTime == null || "".equals(startTime)) {
            Date temp = DateUtil.getBeforeDay(endDate, defaultLimitDay);
            if (DateUtil.getNowDate().getTime() < temp.getTime()) {
                startDate = DateUtil.getNowDate();
            } else {
                startDate = temp;
            }
        } else {
            startDate = DateUtil.formatStrToDate(startTime, timeFormat);
        }
        DateUtil.validate(startDate, endDate, defaultLimitDay, maxLimitDay);
        return new Date[]{startDate, endDate};
    }

    /**
     * 校验时间
     *
     * @param startDate 参数
     * @param endDate 参数
     */
    private static void validate(Date startDate, Date endDate, Integer defaultLimitDay, Integer maxLimitDay) {
        if (DateUtil.getNowDate().getTime() < startDate.getTime()) {
            throw new ServiceException("开始时间不允许为未来时间");
        }
        if (startDate.getTime() > endDate.getTime()) {
            throw new ServiceException("开始时间必须小于结束时间");
        }
        if (DateUtil.getBeforeDay(endDate, maxLimitDay).getTime() > startDate.getTime()) {
            throw new ServiceException("查询最长周期为:" + maxLimitDay + "天");
        }
    }

}

package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;

import java.util.Date;

/**
 * @author wangcheng
 * @date 2018/6/7
 */
public class DateVerifyUtil {

    /**
     * 校验时间返回校验成功的时间
     *
     * @param startTime
     * @param endTime
     * @param defaultLimitDay
     * @param maxLimitDay
     * @param timeFormat
     * @return
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
        DateVerifyUtil.validate(startDate, endDate, defaultLimitDay, maxLimitDay);
        return new Date[]{startDate, endDate};
    }

    /**
     * 校验时间
     *
     * @param startDate
     * @param endDate
     */
    private static void validate(Date startDate, Date endDate, Integer defaultLimitDay, Integer maxLimitDay) {

        if (DateUtil.getNowDate().getTime() < startDate.getTime()) {
            throw new IllegalArgumentException("开始时间不允许为未来时间");
        }
        if (startDate.getTime() > endDate.getTime()) {
            throw new IllegalArgumentException("开始时间必须小于结束时间");
        }
        if (DateUtil.getBeforeDay(endDate, maxLimitDay).getTime() > startDate.getTime()) {
            throw new ServiceException("查询最长周期为:" + maxLimitDay + "天");
        }
    }
}

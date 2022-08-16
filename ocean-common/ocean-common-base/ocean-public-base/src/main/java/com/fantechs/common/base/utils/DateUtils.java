package com.fantechs.common.base.utils;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.text.ParseException;
import java.util.Date;

/* 日期工具类
 * @author sunny
 * @date 2015-9-8
 */
public class DateUtils extends DateFormatUtils {

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * ISO8601 formatter for date-time without time zone.
     * The format used is <tt>yyyy-MM-dd HH:mm:ss</tt>.
     * This pattern does not comply with the formal ISO8601 specification
     * as the standard requires the 'T' prefix for times.
     */
    public static final FastDateFormat ISO_DATETIME_TIME_ZONE__NO_T_FORMAT
            = FastDateFormat.getInstance(DATETIME_PATTERN);


    /**
     * The format used is <tt>yyyy-MM-dd HH:mm:ss</tt>
     *
     * @param date
     * @return
     */
    public static String getDateTimeString(Date date) {

        if (date == null) {
            throw new NullPointerException("要转换的日期类型不能为空");
        }

        return ISO_DATETIME_TIME_ZONE__NO_T_FORMAT.format(date);
    }

    /**
     * The format used is <tt>yyyy-MM-dd</tt>
     *
     * @param date
     * @return
     */
    public static String getDateString(Date date) {

        if (date == null) {
            throw new NullPointerException("要转换的日期类型不能为空");
        }

        return ISO_DATE_FORMAT.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date   时间对象
     * @param format 字符串格式
     * @return 字符串
     */
    public static String getDateString(Date date, String format) {

        return FastDateFormat.getInstance(format).format(date);
    }

    /**
     * 日期格式字符串格式化
     * The format used is <tt>yyyy-MM-dd</tt>.
     *
     * @param str 日期字符串
     * @return 时间对象
     * @throws ParseException
     */
    public static Date getStrToDate(String str) throws ParseException {

        return org.apache.commons.lang.time.DateUtils.parseDate(str, new String[]{DATE_PATTERN});
    }

    /**
     * 日期格式字符串格式化
     * The format used is <tt>yyyy-MM-dd HH:mm:ss</tt>.
     *
     * @param str 日期字符串
     * @return 时间对象
     * @throws ParseException
     */
    public static Date getStrToDateTime(String str) throws ParseException {

        return org.apache.commons.lang.time.DateUtils.parseDate(str, new String[]{DATETIME_PATTERN});
    }

    /**
     * 按指定格式格式化时间字符串
     *
     * @param format 格式化字符串
     * @param str    时间字符串
     * @return 时间对象
     * @throws ParseException
     */
    public static Date getStrToDate(String format, String str) throws ParseException {

        return org.apache.commons.lang.time.DateUtils.parseDate(str, new String[]{format});
    }

    /**
     * 毫秒转DATETIME字符串
     * The format used is <tt>yyyy-MM-dd HH:mm:ss</tt>.
     *
     * @param millis 毫米数
     * @return 字符串
     */
    public static String long2String(Long millis) {

        return ISO_DATETIME_TIME_ZONE__NO_T_FORMAT.format(millis);
    }

    /**
     * 毫秒字符串转DATETIME字符串
     *
     * @param format 格式
     * @param time   毫米数
     * @return 字符串
     */
    public static String long2String(String time) {

        return long2String(Long.valueOf(time));
    }

    /**
     * 毫秒转日期
     *
     * @param millis 毫秒数
     * @return 日期
     */
    public static Date long2Date(Long millis) {

        return new Date(millis);
    }

	/**
	 * 获取该时间点的当天Date对象
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getDayDate(Date date) throws ParseException {
        String dateStr = FastDateFormat.getInstance(DATE_PATTERN).format(date);
        return getStrToDate(dateStr);
    }
}

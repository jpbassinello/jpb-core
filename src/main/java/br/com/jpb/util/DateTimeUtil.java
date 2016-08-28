/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.util;

import com.google.common.collect.Range;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public final class DateTimeUtil {

	public static final DateTimeFormatter DASH_DATE_FORMATTER = DateTimeFormat
			.forPattern(MessageUtil.getString("dash.view.dateFormat"));
	public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormat
			.forPattern(MessageUtil.getString("default.view.dateFormat"));
	public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormat
			.forPattern(MessageUtil.getString("default.view.dateTimeFormat"));

	public static final DateTimeFormatter DASH_ISO_DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	public static final DateTimeFormatter DASH_ISO_DATE_TIME_FORMATTER = DateTimeFormat
			.forPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeZone DEFAULT_DATE_TIME_ZONE = DateTimeZone.UTC;

	private DateTimeUtil() {
	}

	public static String getDefaultViewFormattedLocalDate(LocalDate date) {
		return DEFAULT_DATE_FORMATTER.print(date);
	}

	public static String getDefaultViewFormattedLocalDateTime(LocalDateTime date) {
		return DEFAULT_DATE_TIME_FORMATTER.print(date);
	}

	public static LocalDate getLocalDateByDefaultString(String day) {
		return DEFAULT_DATE_FORMATTER.parseLocalDate(day);
	}

	public static LocalDate getLocalDateByDashString(String day) {
		return DASH_DATE_FORMATTER.parseLocalDate(day);
	}

	public static String getDashStringByLocalDate(LocalDate day) {
		return DASH_DATE_FORMATTER.print(day);
	}

	public static String nowDefaultViewFormattedLocalDateTime() {
		return getDefaultViewFormattedLocalDateTime(nowWithDateTimeInUTC());
	}

	public static LocalDateTime toLocaDateTimeInUTC(Date date) {
		return new DateTime(date, DEFAULT_DATE_TIME_ZONE).toLocalDateTime();
	}

	public static String getIsoFormattedLocalDateTime(LocalDateTime date) {
		return DASH_ISO_DATE_TIME_FORMATTER.print(date);
	}

	public static LocalDateTime nowLocalDateTimeInUserTimeZone(int dateTimeZoneIdentifier) {
		return new DateTime(DateTimeZone.forOffsetHours(dateTimeZoneIdentifier)).toLocalDateTime();
	}

	public static LocalDateTime toLocalDateTimeInUserTimeZone(Date date, int dateTimeZoneIdentifier) {
		return new DateTime(date, DateTimeZone.forOffsetHours(dateTimeZoneIdentifier)).toLocalDateTime();
	}

	public static LocalDateTime nowWithDateTimeInUTC() {
		return new DateTime(DEFAULT_DATE_TIME_ZONE).toLocalDateTime();
	}

	public static Date minusDays(Date date, int days) {
		return new LocalDate(date).minusDays(days).toDate();
	}

	public static LocalDateTime getStartLocalDateTimeOfFilter(LocalDate localDate, int userTimeZone) {
		return localDate == null ? null : getStartLocalDateTimeOfFilter(localDate.toLocalDateTime(LocalTime.MIDNIGHT),
				userTimeZone);
	}

	public static LocalDateTime getStartLocalDateTimeOfFilter(LocalDateTime localDateTime, int userTimeZone) {
		int invertedUserTimeZone = userTimeZone * -1;
		return localDateTime == null ? null : DateTimeUtil
				.toLocalDateTimeInUserTimeZone(localDateTime.toDate(), invertedUserTimeZone);
	}

	public static LocalDateTime getEndLocalDateTimeOfFilter(LocalDate localDate, int userTimeZone) {
		return localDate == null ? null : getEndLocalDateTimeOfFilter(
				localDate.toLocalDateTime(LocalTime.MIDNIGHT).minusMillis(1).plusDays(1), userTimeZone);
	}

	public static LocalDateTime getEndLocalDateTimeOfFilter(LocalDateTime localDateTime, int userTimeZone) {
		int invertedUserTimeZone = userTimeZone * -1;
		return localDateTime == null ? null : DateTimeUtil
				.toLocalDateTimeInUserTimeZone(localDateTime.toDate(), invertedUserTimeZone);
	}

	public static int getNumberOfDaysInMonth(YearMonth yearMonth) {
		return yearMonth.plusMonths(1).toLocalDate(1).minusDays(1).getDayOfMonth();
	}

	public static LocalDateTime getStartOfMonth(YearMonth yearMonth) {
		return yearMonth.toLocalDate(1).toLocalDateTime(LocalTime.MIDNIGHT);
	}

	public static LocalDateTime getEndOfMonth(YearMonth yearMonth) {
		return yearMonth.plusMonths(1).toLocalDate(1).toLocalDateTime(LocalTime.MIDNIGHT).minusMillis(1);
	}

	public static int getDaylightSavings() {
		return TimeZone.getTimeZone("America/Sao_Paulo").inDaylightTime(new Date()) ? 1 : 0;
	}

	public static Date parseInPattern(String value, String pattern) {
		return DateTimeFormat.forPattern(pattern).parseLocalDateTime(value).toDate();
	}

	public static String formatInPattern(LocalDate localDate, String pattern) {
		return DateTimeFormat.forPattern(pattern).print(localDate);
	}

	public static String formatInPattern(LocalDateTime localDateTime, String pattern) {
		return DateTimeFormat.forPattern(pattern).print(localDateTime);
	}

	public static Range<LocalDateTime> dayWithUserTz(LocalDate day, int timeZone) {
		return Range.closed(toLocalDateTimeInUserTimeZone(day.toDate(), timeZone),
				toLocalDateTimeInUserTimeZone(day.plusDays(1).toDate(), timeZone));
	}

	public static Range<LocalDate> weekOfDaySundayAsFirstDay(LocalDate day) {
		LocalDate nextSaturday = day.getDayOfWeek() == DateTimeConstants.SUNDAY ? day.plusDays(6) : day
				.withDayOfWeek(DateTimeConstants.SATURDAY);
		LocalDate previousSunday = nextSaturday.minusDays(6);

		return Range.closed(previousSunday, nextSaturday);
	}

	public static Range<LocalDate> monthOfDay(LocalDate day) {
		return Range.closed(day.withDayOfMonth(1), day.dayOfMonth().withMaximumValue());
	}

	public static Range<LocalDateTime> weekOfDaySundayAsFirstDayWithUserTz(LocalDate day, int timeZone) {
		Range<LocalDate> week = weekOfDaySundayAsFirstDay(day);
		LocalDateTime firstDay = toLocalDateTimeInUserTimeZone(week.lowerEndpoint().toDate(), timeZone);
		LocalDateTime lastDay = toLocalDateTimeInUserTimeZone(week.upperEndpoint().toDate(), timeZone);
		return Range.closed(firstDay, lastDay);
	}

	public static Range<LocalDateTime> monthOfDayWithUserTz(LocalDate day, int timeZone) {
		Range<LocalDate> month = monthOfDay(day);
		LocalDateTime firstDay = toLocalDateTimeInUserTimeZone(month.lowerEndpoint().toDate(), timeZone);
		LocalDateTime lastDay = toLocalDateTimeInUserTimeZone(month.upperEndpoint().toDate(), timeZone);
		return Range.closed(firstDay, lastDay);
	}
}
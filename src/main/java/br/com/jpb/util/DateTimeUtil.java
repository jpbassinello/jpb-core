/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.util;

import br.com.jpb.component.Messages;
import com.google.common.base.Strings;
import com.google.common.collect.Range;
import lombok.experimental.UtilityClass;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@UtilityClass
public class DateTimeUtil {

	public static final DateTimeFormatter DASH_DATE_FORMATTER = DateTimeFormatter
			.ofPattern(Messages.getMessage("dash.view.dateFormat"));
	public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter
			.ofPattern(Messages.getMessage("default.view.dateFormat"));
	public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter
			.ofPattern(Messages.getMessage("default.view.dateTimeFormat"));

	public static final DateTimeFormatter DASH_ISO_DATE_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter DASH_ISO_DATE_TIME_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static String getDefaultViewFormattedLocalDate(LocalDate date) {
		if (date == null) {
			return null;
		}
		return DEFAULT_DATE_FORMATTER.format(date);
	}

	public static String getDefaultViewFormattedLocalDateTime(LocalDateTime date) {
		if (date == null) {
			return null;
		}
		return DEFAULT_DATE_TIME_FORMATTER.format(date);
	}

	public static LocalDate getLocalDateByDefaultString(String day) {
		if (Strings.isNullOrEmpty(day)) {
			return null;
		}
		return LocalDate.parse(day, DEFAULT_DATE_FORMATTER);
	}

	public static LocalDate getLocalDateByDashString(String day) {
		if (Strings.isNullOrEmpty(day)) {
			return null;
		}
		return LocalDate.parse(day, DASH_DATE_FORMATTER);
	}

	public static String getDashStringByLocalDate(LocalDate day) {
		if (day == null) {
			return null;
		}
		return DASH_DATE_FORMATTER.format(day);
	}

	public static String getIsoFormattedLocalDateTime(LocalDateTime date) {
		if (date == null) {
			return null;
		}
		return DASH_ISO_DATE_TIME_FORMATTER.format(date);
	}

	public static int getNumberOfDaysInMonth(YearMonth yearMonth) {
		return yearMonth
				.plusMonths(1)
				.atDay(1)
				.minusDays(1)
				.getDayOfMonth();
	}

	public static LocalDateTime atStartOfMonth(YearMonth yearMonth) {
		return yearMonth
				.atDay(1)
				.atTime(LocalTime.MIDNIGHT);
	}

	public static LocalDateTime atEndOfMonth(YearMonth yearMonth) {
		return yearMonth
				.plusMonths(1)
				.atDay(1)
				.minusDays(1)
				.atTime(LocalTime.MAX);
	}

	public static boolean isSaoPauloBrazilInDaylightSavings() {
		return isNowDaylightSavings("America/Sao_Paulo");
	}

	public static boolean isNowDaylightSavings(String timeZoneId) {
		return TimeZone
				.getTimeZone(timeZoneId)
				.inDaylightTime(new Date());
	}

	public static String formatInPattern(LocalDate localDate, String pattern) {
		if (localDate == null) {
			return null;
		}
		return localDate.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String formatInPattern(LocalDateTime localDateTime, String pattern) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static Range<LocalDate> weekOfDaySundayAsFirstDay(LocalDate day) {
		LocalDate nextSaturday = day.getDayOfWeek() == DayOfWeek.SUNDAY ? day.plusDays(6) : day
				.with(ChronoField.DAY_OF_WEEK, DayOfWeek.SATURDAY.getValue());
		LocalDate previousSunday = nextSaturday.minusDays(6);

		return Range.closed(previousSunday, nextSaturday);
	}

	public static Range<LocalDate> monthOfDay(LocalDate day) {
		return Range
				.closed(day.withDayOfMonth(1), atEndOfMonth(YearMonth.of(day.getYear(), day.getMonth())).toLocalDate());
	}

	public static Range<LocalDateTime> weekOfDaySundayAsFirstDayWithUserTz(LocalDate day) {
		Range<LocalDate> week = weekOfDaySundayAsFirstDay(day);
		return Range.closed(week
				.lowerEndpoint()
				.atStartOfDay(), week
				.upperEndpoint()
				.atTime(LocalTime.MAX));
	}

	public static Range<LocalDateTime> monthOfDayWithUserTz(LocalDate day) {
		Range<LocalDate> month = monthOfDay(day);
		return Range.closed(month
				.lowerEndpoint()
				.atStartOfDay(), month
				.upperEndpoint()
				.atTime(LocalTime.MAX));
	}

	public static LocalDateTime from(Date date) {
		if (date == null) {
			return null;
		}
		return date
				.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}

	public static Date from(LocalDateTime date) {
		if (date == null) {
			return null;
		}
		return Date.from(date
				.atZone(ZoneId.systemDefault())
				.toInstant());
	}
}

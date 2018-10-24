package br.com.jpb.util.test;

import br.com.jpb.util.DateTimeUtil;
import com.google.common.collect.Range;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class DateTimeUtilTest {

	@Test
	public void testWeekOfDaySundayAsFirstDay() {
		LocalDate d1 = LocalDate.of(2015, 6, 12);
		Range<LocalDate> w1 = DateTimeUtil.weekOfDaySundayAsFirstDay(d1);

		Assert.assertEquals(LocalDate.of(2015, 6, 7), w1.lowerEndpoint());
		Assert.assertEquals(LocalDate.of(2015, 6, 13), w1.upperEndpoint());

		LocalDate d2 = LocalDate.of(2015, 6, 7);
		Range<LocalDate> w2 = DateTimeUtil.weekOfDaySundayAsFirstDay(d2);

		Assert.assertEquals(LocalDate.of(2015, 6, 7), w2.lowerEndpoint());
		Assert.assertEquals(LocalDate.of(2015, 6, 13), w2.upperEndpoint());

		LocalDate d3 = LocalDate.of(2015, 6, 13);
		Range<LocalDate> w3 = DateTimeUtil.weekOfDaySundayAsFirstDay(d3);

		Assert.assertEquals(LocalDate.of(2015, 6, 7), w3.lowerEndpoint());
		Assert.assertEquals(LocalDate.of(2015, 6, 13), w3.upperEndpoint());
	}
}

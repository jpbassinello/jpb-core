/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.util.test;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import br.com.jpb.util.DateTimeUtil;

import com.google.common.collect.Range;

/**
 * 
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class DateTimeUtilTest {

	@Test
	public void testWeekOfDaySundayAsFirstDay() {
		LocalDate d1 = new LocalDate(2015, 6, 12);
		Range<LocalDate> w1 = DateTimeUtil.weekOfDaySundayAsFirstDay(d1);

		Assert.assertEquals(new LocalDate(2015, 6, 7), w1.lowerEndpoint());
		Assert.assertEquals(new LocalDate(2015, 6, 13), w1.upperEndpoint());
		
		LocalDate d2 = new LocalDate(2015, 6, 7);
		Range<LocalDate> w2 = DateTimeUtil.weekOfDaySundayAsFirstDay(d2);

		Assert.assertEquals(new LocalDate(2015, 6, 7), w2.lowerEndpoint());
		Assert.assertEquals(new LocalDate(2015, 6, 13), w2.upperEndpoint());
		
		LocalDate d3 = new LocalDate(2015, 6, 13);
		Range<LocalDate> w3 = DateTimeUtil.weekOfDaySundayAsFirstDay(d3);

		Assert.assertEquals(new LocalDate(2015, 6, 7), w3.lowerEndpoint());
		Assert.assertEquals(new LocalDate(2015, 6, 13), w3.upperEndpoint());
	}
}
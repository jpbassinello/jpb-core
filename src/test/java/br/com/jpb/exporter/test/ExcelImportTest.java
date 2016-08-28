/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.exporter.test;

import br.com.jpb.exporter.ExporterColumn;
import br.com.jpb.exporter.ExporterDateTime;
import br.com.jpb.exporter.ExporterNumeric;
import br.com.jpb.exporter.excel.ExcelImporter;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class ExcelImportTest {

	@Test
	public void testImport() {

		List<TestPojo> expected = new ArrayList<>();
		expected.add(new TestPojo(null, "abc", 123, new LocalDate(1985, 4, 10)));
		expected.add(new TestPojo(1, "def", null, new LocalDate(1988, 5, 24)));
		expected.add(new TestPojo(2, "ghi", 456, null));

		File file = FileUtils.toFile(getClass().getResource("/excel/excel-test-1.xlsx"));

		List<TestPojo> processed = new ExcelImporter<>(TestPojo.class).importFile(file);

		Assert.assertEquals(expected, processed);

	}

	public static class TestPojo {

		@ExporterColumn(headerText = "Coluna 1", index = 0)
		@ExporterNumeric
		private Integer c1;
		@ExporterColumn(headerText = "Coluna 2", index = 1)
		private String c2;
		@ExporterColumn(headerText = "Coluna 3", index = 2)
		@ExporterNumeric
		private Integer c3;
		@ExporterColumn(headerText = "Coluna 4", index = 4)
		@ExporterDateTime(format = "MM/dd/yyyy")
		private LocalDate c4;
		@ExporterColumn(headerText = "Coluna 5", index = 5)
		@ExporterDateTime(format = "MM/dd/yyyy")
		private LocalDate c5;

		public TestPojo() {
		}

		public TestPojo(Integer c1, String c2, Integer c3, LocalDate c5) {
			this.c1 = c1;
			this.c2 = c2;
			this.c3 = c3;
			this.c5 = c5;
		}

		public Integer getC1() {
			return c1;
		}

		public String getC2() {
			return c2;
		}

		public Integer getC3() {
			return c3;
		}

		public LocalDate getC5() {
			return c5;
		}

		@Override
		public int hashCode() {
			return Objects.hash(c1, c2, c3, c5);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof TestPojo)) {
				return false;
			}
			TestPojo other = (TestPojo) obj;
			return Objects.equals(c1, other.c1) && Objects.equals(c2, other.c2) && Objects
					.equals(c3, other.c3) && Objects.equals(c5, other.c5);
		}

	}
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.exporter.test;

import br.com.jpb.exporter.ExporterColumn;
import br.com.jpb.exporter.ExporterDateTime;
import br.com.jpb.exporter.ExporterNumeric;
import br.com.jpb.exporter.txt.TxtExporter;
import br.com.jpb.exporter.txt.TxtImporter;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class TxtTest {

	private static final LocalDateTime DATE_TIME_1 = LocalDate
			.now()
			.atTime(LocalTime.of(10, 0, 0, 0));
	private static final LocalDateTime DATE_TIME_2 = LocalDate
			.now()
			.atTime(LocalTime.of(18, 23, 54, 0));

	@Test
	public void testExportImport() {

		List<TestPojo> expected = new ArrayList<>();
		expected.add(new TestPojo(1, "Coluna 1", true, LocalDate.now(), DATE_TIME_1, new BigDecimal("0.333")));
		expected.add(new TestPojo(2, "Coluna 2", false, LocalDate
				.now()
				.withDayOfMonth(10), DATE_TIME_2,
				new BigDecimal("23.996")));

		testHappyFlow(expected);
		testPipeSeparator(expected);
		testWithIgnoredFields(expected);
	}

	private void testHappyFlow(List<TestPojo> expected) {
		File file = new TxtExporter<>(TestPojo.class).exportFile(expected, "test.csv");

		List<TestPojo> processed = new TxtImporter<>(TestPojo.class).importFile(file);

		Assert.assertEquals(expected, processed);
	}

	private void testWithIgnoredFields(List<TestPojo> expected) {
		final Set<String> ignoredFields = new HashSet<>(Arrays.asList("c1", "c3", "c4", "c6"));
		File fileWithIgnoredFields = new TxtExporter<>(TestPojo.class)
				.withIgnoredFields(ignoredFields)
				.exportFile(expected, "testWithIgnoredFields.csv");

		List<TestPojo> expectedWithIgnoredFields = new ArrayList<>();
		expectedWithIgnoredFields.add(new TestPojo(0, "Coluna 1", false, null, DATE_TIME_1, null));
		expectedWithIgnoredFields.add(new TestPojo(0, "Coluna 2", false, null, DATE_TIME_2, null));

		List<TestPojo> processedWithIgnoredFields = new TxtImporter<>(TestPojo.class)
				.withIgnoredFields(ignoredFields)
				.importFile(fileWithIgnoredFields);

		Assert.assertEquals(expectedWithIgnoredFields, processedWithIgnoredFields);
	}

	private void testPipeSeparator(List<TestPojo> expected) {
		File filePipeSeparator = new TxtExporter<>(TestPojo.class)
				.withSeparator('|')
				.exportFile(expected, "testPipeSeparator.csv");

		List<TestPojo> processedPipeSeparator = new TxtImporter<>(TestPojo.class)
				.withSeparator('|')
				.importFile(filePipeSeparator);

		Assert.assertEquals(expected, processedPipeSeparator);
	}

	public static class TestPojo {

		@ExporterColumn(headerText = "Coluna 1", index = 0)
		@ExporterNumeric
		private int c1;
		@ExporterColumn(headerText = "Coluna 2", index = 1)
		private String c2;
		@ExporterColumn(headerText = "Coluna 3", index = 2)
		private boolean c3;
		@ExporterColumn(headerText = "Coluna 4", index = 3)
		@ExporterDateTime(formatDate = "MM/dd/yyyy")
		private LocalDate c4;
		@ExporterColumn(headerText = "Coluna 5", index = 4)
		@ExporterDateTime
		private LocalDateTime c5;
		@ExporterColumn(headerText = "Coluna 6", index = 5)
		@ExporterNumeric(scale = 3, roundingMode = RoundingMode.HALF_UP)
		private BigDecimal c6;

		public TestPojo() {
		}

		public TestPojo(int c1, String c2, boolean c3, LocalDate c4, LocalDateTime c5, BigDecimal c6) {
			this.c1 = c1;
			this.c2 = c2;
			this.c3 = c3;
			this.c4 = c4;
			this.c5 = c5;
			this.c6 = c6;
		}

		public int getC1() {
			return 29;
		}

		public String getC2() {
			return c2;
		}

		public boolean getC3() {
			return true;
		}

		public LocalDate getC4() {
			return LocalDate.now();
		}

		public LocalDateTime getC5() {
			return LocalDateTime.now();
		}

		public BigDecimal getC6() {
			return new BigDecimal("23.3455");
		}

		@Override
		public int hashCode() {
			return Objects.hash(c1, c2, c3, c4, c5, c6);
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
					.equals(c3, other.c3) && Objects.equals(c4, other.c4) && Objects.equals(c5, other.c5) && Objects
					.equals(c6, other.c6);
		}

	}
}
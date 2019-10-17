package br.com.jpb.data.excel;

import br.com.jpb.data.Importer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ExcelImporter<T> extends Importer<T> {

	private int sheetNumber = 0;
	private Sheet sheet;

	public ExcelImporter(Class<T> clazz) {
		super(clazz);
	}

	public ExcelImporter<T> withSheetNumber(int sheetNumber) {
		this.sheetNumber = sheetNumber;
		return this;
	}

	public ExcelImporter<T> withRowsInHeader(int rowsInHeader) {
		this.rowsInHeader = rowsInHeader;
		return this;
	}

	public ExcelImporter<T> withIgnoredFields(Set<String> ignoredFields) {
		this.ignoredFields.addAll(ignoredFields);
		return this;
	}

	public static List<List<String>> readStringMatrix(File file, int rowsInHeader, int sheetNumber) {
		ExcelImporter<Object> excelImporter = new ExcelImporter<>(Object.class)
				.withRowsInHeader(rowsInHeader)
				.withSheetNumber(sheetNumber);

		try {
			excelImporter.init(new BufferedInputStream(new FileInputStream(file)));
		} catch (IOException e) {
			throw new IllegalStateException("Error while get workbook from Input Stream", e);
		}

		return excelImporter.rows(rowsInHeader);
	}

	@Override
	protected void init(InputStream is) {
		Workbook wb;
		try {
			wb = getWorkbook(is);
		} catch (Exception e) {
			throw new IllegalStateException("Error while get workbook from Input Stream", e);
		}
		sheet = wb.getSheetAt(this.sheetNumber);
	}

	@Override
	protected List<List<String>> rows(int rowsInHeader) {
		Iterator<Row> rowIterator = sheet.iterator();
		final List<List<String>> rows = new ArrayList<>();
		int rowIndex = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (rowIndex++ < rowsInHeader) {
				continue;
			}
			List<String> columns = new ArrayList<>();
			for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
				Cell cell = row.getCell(colIndex);
				if (cell == null || cell.getCellType() == CellType.BLANK) {
					columns.add(null);
					continue;
				}
				try {
					switch (cell.getCellType()) {
						case STRING:
							columns.add(cell.getStringCellValue());
							break;
						case NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
								columns.add(Long
										.valueOf(cell
												.getDateCellValue()
												.getTime())
										.toString());
							} else {
								double d = cell.getNumericCellValue();
								if (d % 1 == 0) {
									columns.add(Long
											.valueOf(Double
													.valueOf(d)
													.longValue())
											.toString());
								} else {
									columns.add(Double
											.valueOf(d)
											.toString());
								}
							}
							break;
						case BOOLEAN:
							columns.add(Boolean
									.valueOf(cell.getBooleanCellValue())
									.toString());
							break;
						default:
							break;
					}
				} catch (Exception e) {
					throw new IllegalStateException(String.format(
							"Error while adding cell value to List. Problably a developer error. " + "Check your bean " +
									"types and annotations. Excel [Row, Col]: [%s, %s]",
							rowIndex, colIndex), e);
				}
			}
			rows.add(columns);
		}
		return rows;
	}

	private Workbook getWorkbook(InputStream inputStream) {
		Workbook wb;
		try {
			wb = WorkbookFactory.create(inputStream);
		} catch (Exception e) {
			throw new IllegalStateException("Error while create workbook.", e);
		}
		return wb;
	}

}

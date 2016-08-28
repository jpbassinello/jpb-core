package br.com.jpb.exporter.excel;

import br.com.jpb.exporter.Importer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ExcelImporter<T> extends Importer<T> {

	private int sheetNumber = 0;
	private Workbook wb;
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

	@Override
	protected void init(InputStream is) {
		try {
			wb = getWorkbook(is);
		} catch (IOException e) {
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
				if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					columns.add(null);
					continue;
				}
				try {
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							columns.add(cell.getStringCellValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
								columns.add(Long.valueOf(cell.getDateCellValue().getTime()).toString());
							} else {
								double d = cell.getNumericCellValue();
								if (d % 1 == 0) {
									columns.add(Long.valueOf(Double.valueOf(d).longValue()).toString());
								} else {
									columns.add(Double.valueOf(d).toString());
								}
							}
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							columns.add(Boolean.valueOf(cell.getBooleanCellValue()).toString());
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

	private Workbook getWorkbook(InputStream inputStream) throws IOException {
		Workbook wb;
		try {
			wb = WorkbookFactory.create(inputStream);
		} catch (InvalidFormatException e) {
			throw new IllegalStateException("Error while create workbook.", e);
		}
		return wb;
	}

}

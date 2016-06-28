package br.com.jpb.exporter.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import br.com.jpb.exporter.Exporter;

public class ExcelExporter<T> extends Exporter<T> {

	private static final int MAX_TEXT_COLUMN_SIZE = 32767;

	private String sheetName = "Dados";
	private Workbook wb;
	private Sheet sheet;
	private Row headerRow;
	private CellStyle headerCellStyle;
	private CellStyle defaultCellStyle;
	private Map<String, CellStyle> dateTimeStylesByFormatter = new HashMap<>();

	private final Map<Integer, Row> rowByIndex = new HashMap<>();

	public ExcelExporter(Class<T> clazz) {
		super(clazz);
	}

	public ExcelExporter<T> withNoHeader(boolean noHeader) {
		this.noHeader = noHeader;
		return this;
	}

	public ExcelExporter<T> withSheetName(String sheetName) {
		this.sheetName = sheetName;
		return this;
	}

	public ExcelExporter<T> withIgnoredFields(Set<String> ignoredFields) {
		this.ignoredFields.addAll(ignoredFields);
		return this;
	}

	@Override
	protected void init() {
		wb = new HSSFWorkbook();
		sheet = wb.createSheet(sheetName);

		defaultCellStyle = defaultCellStyle(wb);
		headerCellStyle = defaultHeaderStyle(wb);
	}

	@Override
	protected void initHeader() {
		headerRow = sheet.createRow(0);
	}

	@Override
	protected void writeHeaderColumn(String value, int colIndex) {
		Cell cell = headerRow.createCell(colIndex);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue(value);
	}

	@Override
	protected void writeEmptyColumn(int rowIndex, int colIndex) {
		Cell cell = defaultCell(rowIndex, colIndex);
		cell.setCellValue("");
	}

	@Override
	protected void writeColumn(Date value, String dateFormat, int rowIndex,
			int colIndex) {

		CellStyle dateTimeCellStyle = dateTimeStylesByFormatter.get(dateFormat);
		if (dateTimeCellStyle == null) {
			dateTimeCellStyle = defaultCellStyle(wb);
			CreationHelper createHelper = wb.getCreationHelper();
			dateTimeCellStyle.setDataFormat(
					createHelper.createDataFormat().getFormat(dateFormat));

			dateTimeStylesByFormatter.put(dateFormat, dateTimeCellStyle);
		}
		Cell cell = getRow(rowIndex).createCell(colIndex);
		cell.setCellStyle(dateTimeCellStyle);

		cell.setCellValue(value);
	}

	@Override
	protected void writeColumn(double value, int rowIndex, int colIndex) {
		Cell cell = defaultCell(rowIndex, colIndex);
		cell.setCellValue(value);
	}

	@Override
	protected void writeColumn(boolean value, int rowIndex, int colIndex) {
		Cell cell = defaultCell(rowIndex, colIndex);
		cell.setCellValue(value);
	}

	@Override
	protected void writeColumn(String value, int rowIndex, int colIndex) {
		Cell cell = defaultCell(rowIndex, colIndex);
		if (value != null && value.length() > MAX_TEXT_COLUMN_SIZE) {
			cell.setCellValue(value.substring(0, MAX_TEXT_COLUMN_SIZE - 1));
		} else {
			cell.setCellValue(value);
		}
	}

	@Override
	protected void writeTheFile(OutputStream os) {
		try {
			autoSizeColumns(wb);
			wb.write(os);
		} catch (IOException e) {
			throw new IllegalStateException("Error while writing the file", e);
		}
	}

	private void autoSizeColumns(Workbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				Row row = sheet.getRow(0);
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					sheet.autoSizeColumn(columnIndex);
				}
			}
		}
	}

	@Override
	protected void close() {
		try {
			wb.close();
		} catch (IOException e) {
			throw new IllegalStateException("Error while closing the WorkBook",
					e);
		}
	}

	private Cell defaultCell(int rowIndex, int colIndex) {
		Cell cell = getRow(rowIndex).createCell(colIndex);
		cell.setCellStyle(defaultCellStyle);

		return cell;
	}

	private synchronized Row getRow(int rowIndex) {
		Row row = rowByIndex.get(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
			rowByIndex.put(rowIndex, row);
		}
		return row;
	}

	private CellStyle defaultHeaderStyle(Workbook wb) {
		CellStyle headerStyle = defaultCellStyle(wb);
		headerStyle.setFillForegroundColor(
				IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);

		return headerStyle;
	}

	private CellStyle defaultCellStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}

	public File exportDataMatrixToFile(List<String> headers,
			List<List<String>> matrix, String fileName) {
		init();

		initHeader();
		for (int i = 0; i < headers.size(); i++) {
			writeHeaderColumn(headers.get(i), i);
		}

		for (int i = 0; i < matrix.size(); i++) {
			List<String> row = matrix.get(i);
			for (int j = 0; j < row.size(); j++) {
				String obj = row.get(j);
				obj = obj == null ? "" : obj;
				writeColumn(obj, i + 1, j);
			}
		}

		return createFile(fileName);
	}
}

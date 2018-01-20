package com.gc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class WorkbookLoader {

	public static final String XLS_EXTENSION = "xls";

	public static final String XLSX_EXTENSION = "xlsx";

	public static Workbook loadWorkbook(File file) throws IOException {
		String extension = FilenameUtils.getExtension(file.getAbsolutePath());
		FileInputStream fileInputStream = new FileInputStream(file);
		if (XLS_EXTENSION.equalsIgnoreCase(extension)) {
			return new HSSFWorkbook(fileInputStream);
		} else if (XLSX_EXTENSION.equalsIgnoreCase(extension)) {
			return new XSSFWorkbook(fileInputStream);
		}
		fileInputStream.close();
		throw new IOException(String.format("Not an Excel file: %s", file.getAbsolutePath()));
	}

}

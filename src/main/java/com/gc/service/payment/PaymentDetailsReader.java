package com.gc.service.payment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gc.util.Formats;
import com.gc.util.WorkbookLoader;
import com.gc.vo.payment.PaymentDetail;
import com.gc.vo.payment.PaymentSheetConfig;
import com.gc.vo.payment.PaymentSheetConfig.PaymentSheetCellConfig;

public class PaymentDetailsReader {

	private PaymentSheetConfig paymentSheetConfig;

	public PaymentDetailsReader(PaymentSheetConfig paymentSheetConfig) {
		this.paymentSheetConfig = paymentSheetConfig;
	}

	public Map<String, List<PaymentDetail>> read(File memberPaymentsFile) throws FileNotFoundException, IOException {
		MultiValueMap<String, PaymentDetail> returnValue = new LinkedMultiValueMap<>();
		try (Workbook wb = WorkbookLoader.loadWorkbook(memberPaymentsFile)) {
			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			for (int i = 0; i < paymentSheetConfig.getSkipRows(); i++) {
				rows.next();
			}
			while (rows.hasNext()) {
				Row row = rows.next();
				PaymentDetail pd = buildPaymentDetail(row);
				if (StringUtils.isNotBlank(pd.getFlatNo())) {
					returnValue.add(pd.getFlatNo(), pd);
				}
			}
		}
		return returnValue;
	}

	private PaymentDetail buildPaymentDetail(Row row) {
		PaymentSheetCellConfig cellConfig = paymentSheetConfig.getCell();
		PaymentDetail pd = new PaymentDetail();

		Iterator<Cell> cellIterator = row.cellIterator();
		int cellCounter = 0;
		while (cellIterator.hasNext() && cellCounter < cellConfig.getMaxCells()) {
			Cell cell = cellIterator.next();
			if (cellCounter == cellConfig.getVoucherNoCell()) {
				pd.setVoucherNo(readVoucherNo(cell));
			} else if (cellCounter == cellConfig.getRcvdDateCell()) {
				pd.setPaymentDate(readDate(cell));
			} else if (cellCounter == cellConfig.getAmountCell()) {
				pd.setAmount(readAmount(cell));
			} else if (cellCounter == cellConfig.getChequeNoCell()) {
				pd.setCheckNo(readCheckNo(cell));
			} else if (cellCounter == cellConfig.getFlatNoCell()) {
				pd.setFlatNo(readFlatNo(cell));
			}
			cellCounter++;
		}
		return pd;
	}

	private int readVoucherNo(Cell cell) {
		double numericCellValue = cell.getNumericCellValue();
		return Integer.parseInt(Formats.PHONE_NUMBER_FORMATTER.format(numericCellValue));
	}

	private Date readDate(Cell cell) {

		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			}
		} else if (cell.getCellTypeEnum() == CellType.STRING) {
			String cellValue = cell.getStringCellValue();
			if (StringUtils.isNotBlank(cellValue)) {
				return Formats.interpretDate(cellValue);
			}
		}
		return null;
	}

	private double readAmount(Cell cell) {
		return cell.getNumericCellValue();
	}

	private String readCheckNo(Cell cell) {
		return readNonBlankString(cell);
	}

	private String readFlatNo(Cell cell) {
		String stringCellValue = cell.getStringCellValue();
		return Formats.interpretFlatNumber(stringCellValue);
	}

	private String readNonBlankString(Cell cell) {
		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			return Formats.PHONE_NUMBER_FORMATTER.format(cell.getNumericCellValue());
		} else if (cell.getCellTypeEnum() == CellType.STRING) {
			String stringCellValue = cell.getStringCellValue();
			if (StringUtils.isNotBlank(stringCellValue)) {
				return stringCellValue.trim();
			}
		}
		return null;
	}

}

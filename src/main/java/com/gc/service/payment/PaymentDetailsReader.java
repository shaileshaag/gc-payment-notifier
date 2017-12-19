package com.gc.service.payment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gc.util.Formats;
import com.gc.vo.payment.PaymentDetail;

public class PaymentDetailsReader {

	@Value("${payment-details.cell.voucher-no-cell}")
	private int voucherNoCell;

	@Value("${payment-details.cell.rcvd-date-cell}")
	private int rcvdDateCell;

	@Value("${payment-details.cell.amount-cell}")
	private int amountCell;

	@Value("${payment-details.cell.cheque-no-cell}")
	private int chequeNoCell;

	@Value("${payment-details.cell.flat-no-cell}")
	private int flatNoCell;

	@Value("${payment-details.cell.max-cells}")
	private int maxCellRead;

	public Map<String, List<PaymentDetail>> read(File memberPaymentsFile) throws FileNotFoundException, IOException {
		MultiValueMap<String, PaymentDetail> returnValue = new LinkedMultiValueMap<>();
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(memberPaymentsFile));
		HSSFSheet sheet = wb.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		// Ignore first two rows
		rows.next();
		rows.next();
		while (rows.hasNext()) {
			HSSFRow row = (HSSFRow) rows.next();
			PaymentDetail pd = buildPaymentDetail(row);
			if (StringUtils.isNotBlank(pd.getFlatNo())) {
				returnValue.add(pd.getFlatNo(), pd);
			}
		}
		return returnValue;
	}

	private PaymentDetail buildPaymentDetail(HSSFRow row) {
		PaymentDetail pd = new PaymentDetail();

		Iterator<Cell> cellIterator = row.cellIterator();
		int cellCounter = 0;
		while (cellIterator.hasNext() && cellCounter < maxCellRead) {
			Cell cell = cellIterator.next();
			if (cellCounter == voucherNoCell) {
				pd.setVoucherNo(readVoucherNo(cell));
			} else if (cellCounter == rcvdDateCell) {
				pd.setPaymentDate(readDate(cell));
			} else if (cellCounter == amountCell) {
				pd.setAmount(readAmount(cell));
			} else if (cellCounter == chequeNoCell) {
				pd.setCheckNo(readCheckNo(cell));
			} else if (cellCounter == flatNoCell) {
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
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			}
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
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
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			return Formats.PHONE_NUMBER_FORMATTER.format(cell.getNumericCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			String stringCellValue = cell.getStringCellValue();
			if (StringUtils.isNotBlank(stringCellValue)) {
				return stringCellValue.trim();
			}
		}
		return null;
	}

}

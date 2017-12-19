package com.gc.service.pending;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gc.util.Formats;
import com.gc.vo.pending.PendingDetail;

public class PendingDetailsReader {

	@Value("${pending-details.cell.amount-cell}")
	private int amountCell;

	@Value("${pending-details.cell.flat-no-cell}")
	private int flatNoCell;

	@Value("${pending-details.cell.max-cells}")
	private int maxCellRead;

	public Map<String, List<PendingDetail>> read(File memberPaymentsFile, Date pendingSince) throws FileNotFoundException, IOException {
		MultiValueMap<String, PendingDetail> returnValue = new LinkedMultiValueMap<>();
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(memberPaymentsFile));
		HSSFSheet sheet = wb.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		// Ignore first two rows
		rows.next();
		rows.next();
		while (rows.hasNext()) {
			HSSFRow row = (HSSFRow) rows.next();
			PendingDetail pd = buildPaymentDetail(row, pendingSince);
			if (StringUtils.isNotBlank(pd.getFlatNo())) {
				returnValue.add(pd.getFlatNo(), pd);
			}
		}
		return returnValue;
	}

	private PendingDetail buildPaymentDetail(HSSFRow row, Date pendingSince) {
		PendingDetail pd = new PendingDetail();
		pd.setPendingDate(pendingSince);

		Iterator<Cell> cellIterator = row.cellIterator();
		int cellCounter = 0;
		while (cellIterator.hasNext() && cellCounter < maxCellRead) {
			Cell cell = cellIterator.next();
			if (cellCounter == amountCell) {
				pd.setAmount(readAmount(cell));
			} else if (cellCounter == flatNoCell) {
				pd.setFlatNo(readFlatNo(cell));
			}
			cellCounter++;
		}
		return pd;
	}

	private double readAmount(Cell cell) {
		return cell.getNumericCellValue();
	}

	private String readFlatNo(Cell cell) {
		String stringCellValue = cell.getStringCellValue();
		return Formats.interpretFlatNumber(stringCellValue);
	}

}

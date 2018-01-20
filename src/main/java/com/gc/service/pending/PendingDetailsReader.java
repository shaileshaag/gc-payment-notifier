package com.gc.service.pending;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gc.util.Formats;
import com.gc.util.WorkbookLoader;
import com.gc.vo.pending.PendingDetail;
import com.gc.vo.pending.PendingSheetConfig;
import com.gc.vo.pending.PendingSheetConfig.PendingSheetCellConfig;

public class PendingDetailsReader {

	private PendingSheetConfig pendingSheetConfig;

	public PendingDetailsReader(PendingSheetConfig pendingSheetConfig) {
		this.pendingSheetConfig = pendingSheetConfig;
	}

	public Map<String, List<PendingDetail>> read(File memberOutstandingFile, Date pendingSince)
			throws FileNotFoundException, IOException {
		MultiValueMap<String, PendingDetail> returnValue = new LinkedMultiValueMap<>();
		try (Workbook wb = WorkbookLoader.loadWorkbook(memberOutstandingFile)) {
			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			for (int i = 0; i < pendingSheetConfig.getSkipRows(); i++) {
				rows.next();
			}
			while (rows.hasNext()) {
				Row row = rows.next();
				PendingDetail pd = buildPaymentDetail(row, pendingSince);
				if (StringUtils.isNotBlank(pd.getFlatNo())) {
					returnValue.add(pd.getFlatNo(), pd);
				}
			}
		}
		return returnValue;
	}

	private PendingDetail buildPaymentDetail(Row row, Date pendingSince) {
		PendingDetail pd = new PendingDetail();
		pd.setPendingDate(pendingSince);

		Iterator<Cell> cellIterator = row.cellIterator();
		int cellCounter = 0;
		PendingSheetCellConfig cellConfig = pendingSheetConfig.getCell();
		while (cellIterator.hasNext() && cellCounter < cellConfig.getMaxCells()) {
			Cell cell = cellIterator.next();
			if (cellCounter == cellConfig.getAmountCell()) {
				pd.setAmount(readAmount(cell));
			} else if (cellCounter == cellConfig.getFlatNoCell()) {
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

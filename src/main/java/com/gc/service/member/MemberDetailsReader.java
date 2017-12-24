package com.gc.service.member;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.gc.util.Formats;
import com.gc.vo.member.MemberDetail;
import com.gc.vo.member.MemberSheetConfig;
import com.gc.vo.member.MemberSheetConfig.MemberSheetCellConfig;

public class MemberDetailsReader {

	private MemberSheetConfig memberSheetConfig;

	public MemberDetailsReader(MemberSheetConfig memberSheetConfig) {
		this.memberSheetConfig = memberSheetConfig;
	}

	public List<MemberDetail> read(File memberDetailsFile) throws FileNotFoundException, IOException {
		List<MemberDetail> returnValue = new ArrayList<>();
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(memberDetailsFile));
		HSSFSheet sheet = wb.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		for (int i = 0; i < memberSheetConfig.getSkipRows(); i++) {
			rows.next();
		}
		while (rows.hasNext()) {
			HSSFRow row = (HSSFRow) rows.next();
			MemberDetail md = buildMemberDetail(row);
			if (StringUtils.isNotBlank(md.getFlatNo())) {
				returnValue.add(md);
			}
		}
		return returnValue;
	}

	private MemberDetail buildMemberDetail(HSSFRow row) {
		MemberSheetCellConfig cellConfig = memberSheetConfig.getCell();
		MemberDetail md = new MemberDetail();
		Iterator<Cell> cellIterator = row.cellIterator();
		int cellCounter = 0;
		while (cellIterator.hasNext() && cellCounter < cellConfig.getMaxCells()) {
			Cell cell = cellIterator.next();
			if (cellCounter == cellConfig.getFlatNoCell()) {
				md.setFlatNo(readFlatNo(cell));
			} else if (cellCounter == cellConfig.getMobileNoCell()) {
				md.setMobile(readMobileNo(cell));
			} else if (cellCounter == cellConfig.getEmailIdCell()) {
				md.setEmail(readEmailId(cell));
			}
			cellCounter++;
		}
		return md;
	}

	private String readFlatNo(Cell cell) {
		String stringCellValue = cell.getStringCellValue();
		return Formats.interpretFlatNumber(stringCellValue);
	}

	private String readMobileNo(Cell cell) {
		double numericCellValue = cell.getNumericCellValue();
		if (numericCellValue < 1d) {
			return null;
		}
		return Formats.PHONE_NUMBER_FORMATTER.format(numericCellValue);
	}

	private String readEmailId(Cell cell) {
		String stringCellValue = cell.getStringCellValue();
		if (StringUtils.isNotBlank(stringCellValue)) {
			return stringCellValue.trim();
		}
		return null;
	}

}

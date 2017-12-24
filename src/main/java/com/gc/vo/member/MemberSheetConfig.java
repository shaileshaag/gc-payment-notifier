package com.gc.vo.member;

public class MemberSheetConfig {

	private MemberSheetCellConfig cell;

	private int skipRows;

	public MemberSheetCellConfig getCell() {
		return cell;
	}

	public void setCell(MemberSheetCellConfig cell) {
		this.cell = cell;
	}

	public int getSkipRows() {
		return skipRows;
	}

	public void setSkipRows(int skipRows) {
		this.skipRows = skipRows;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MemberSheetConfig [cell=").append(cell).append(", skipRows=").append(skipRows).append("]");
		return builder.toString();
	}

	public static class MemberSheetCellConfig {

		private int flatNoCell;

		private int mobileNoCell;

		private int emailIdCell;

		private int maxCells;

		public int getFlatNoCell() {
			return flatNoCell;
		}

		public void setFlatNoCell(int flatNoCell) {
			this.flatNoCell = flatNoCell;
		}

		public int getMobileNoCell() {
			return mobileNoCell;
		}

		public void setMobileNoCell(int mobileNoCell) {
			this.mobileNoCell = mobileNoCell;
		}

		public int getEmailIdCell() {
			return emailIdCell;
		}

		public void setEmailIdCell(int emailIdCell) {
			this.emailIdCell = emailIdCell;
		}

		public int getMaxCells() {
			return maxCells;
		}

		public void setMaxCells(int maxCells) {
			this.maxCells = maxCells;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("MemberSheetCellConfig [flatNoCell=").append(flatNoCell).append(", mobileNoCell=")
					.append(mobileNoCell).append(", emailIdCell=").append(emailIdCell).append(", maxCells=")
					.append(maxCells).append("]");
			return builder.toString();
		}

	}

}

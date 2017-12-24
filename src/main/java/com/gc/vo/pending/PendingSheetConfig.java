package com.gc.vo.pending;

public class PendingSheetConfig {

	private PendingSheetCellConfig cell;

	private int skipRows;

	public PendingSheetCellConfig getCell() {
		return cell;
	}

	public void setCell(PendingSheetCellConfig cell) {
		this.cell = cell;
	}

	public int getSkipRows() {
		return skipRows;
	}

	public void setSkipRows(int skipRows) {
		this.skipRows = skipRows;
	}

	public static class PendingSheetCellConfig {

		private int amountCell;

		private int flatNoCell;

		private int maxCells;

		public int getAmountCell() {
			return amountCell;
		}

		public void setAmountCell(int amountCell) {
			this.amountCell = amountCell;
		}

		public int getFlatNoCell() {
			return flatNoCell;
		}

		public void setFlatNoCell(int flatNoCell) {
			this.flatNoCell = flatNoCell;
		}

		public int getMaxCells() {
			return maxCells;
		}

		public void setMaxCells(int maxCells) {
			this.maxCells = maxCells;
		}

	}

}

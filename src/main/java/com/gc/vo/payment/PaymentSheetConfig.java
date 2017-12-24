package com.gc.vo.payment;

public class PaymentSheetConfig {

	private PaymentSheetCellConfig cell;

	private int skipRows;

	public PaymentSheetCellConfig getCell() {
		return cell;
	}

	public void setCell(PaymentSheetCellConfig cell) {
		this.cell = cell;
	}

	public int getSkipRows() {
		return skipRows;
	}

	public void setSkipRows(int skipRows) {
		this.skipRows = skipRows;
	}

	public static class PaymentSheetCellConfig {

		private int amountCell;

		private int chequeNoCell;

		private int flatNoCell;

		private int voucherNoCell;

		private int rcvdDateCell;

		private int maxCells;

		public int getAmountCell() {
			return amountCell;
		}

		public void setAmountCell(int amountCell) {
			this.amountCell = amountCell;
		}

		public int getChequeNoCell() {
			return chequeNoCell;
		}

		public void setChequeNoCell(int chequeNoCell) {
			this.chequeNoCell = chequeNoCell;
		}

		public int getFlatNoCell() {
			return flatNoCell;
		}

		public void setFlatNoCell(int flatNoCell) {
			this.flatNoCell = flatNoCell;
		}

		public int getVoucherNoCell() {
			return voucherNoCell;
		}

		public void setVoucherNoCell(int voucherNoCell) {
			this.voucherNoCell = voucherNoCell;
		}

		public int getRcvdDateCell() {
			return rcvdDateCell;
		}

		public void setRcvdDateCell(int rcvdDateCell) {
			this.rcvdDateCell = rcvdDateCell;
		}

		public int getMaxCells() {
			return maxCells;
		}

		public void setMaxCells(int maxCells) {
			this.maxCells = maxCells;
		}

	}

}

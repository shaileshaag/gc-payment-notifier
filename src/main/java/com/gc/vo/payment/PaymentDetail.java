package com.gc.vo.payment;

import java.util.Date;

public class PaymentDetail {

	private String flatNo;

	private Date paymentDate;

	private Date brsDate;

	private double amount;

	private String checkNo;

	private int voucherNo;

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Date getBrsDate() {
		return brsDate;
	}

	public void setBrsDate(Date brsDate) {
		this.brsDate = brsDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public int getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(int voucherNo) {
		this.voucherNo = voucherNo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PaymentDetail [flatNo=").append(flatNo).append(", paymentDate=").append(paymentDate)
				.append(", brsDate=").append(brsDate).append(", amount=").append(amount).append(", checkNo=")
				.append(checkNo).append(", voucherNo=").append(voucherNo).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + voucherNo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentDetail other = (PaymentDetail) obj;
		if (voucherNo != other.voucherNo)
			return false;
		return true;
	}

}

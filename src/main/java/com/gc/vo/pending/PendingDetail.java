package com.gc.vo.pending;

import java.util.Date;

public class PendingDetail {

	private String flatNo;

	private Date pendingDate;

	private double pendingAmount;

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public Date getPendingDate() {
		return pendingDate;
	}

	public void setPendingDate(Date pendingDate) {
		this.pendingDate = pendingDate;
	}

	public double getAmount() {
		return pendingAmount;
	}

	public void setAmount(double amount) {
		this.pendingAmount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flatNo == null) ? 0 : flatNo.hashCode());
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
		PendingDetail other = (PendingDetail) obj;
		if (flatNo == null) {
			if (other.flatNo != null)
				return false;
		} else if (!flatNo.equals(other.flatNo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PendingDetail [flatNo=").append(flatNo).append(", pendingDate=").append(pendingDate)
				.append(", pendingAmount=").append(pendingAmount).append("]");
		return builder.toString();
	}

}

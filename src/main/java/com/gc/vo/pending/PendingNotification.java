package com.gc.vo.pending;

import java.util.HashMap;
import java.util.Map;

import com.gc.util.Formats;
import com.gc.vo.Notification;

public class PendingNotification extends Notification implements Comparable<PendingNotification> {

	private static final String TEMPLATE_PENDING_AMOUNT = "pending-amount";

	private static final String TEMPLATE_PENDING_DATE = "pending-date";

	public static final String[] HEADERS = { "Flat No.", "Pending Amount", "Send Email", "Send SMS" };

	private PendingDetail pendingDetail;

	public Object[] getTableDataArray() {
		Object emailEntry = getMemberDetail().canSendEmail() ? Boolean.TRUE : "No Email ID";
		Object smsEntry = getMemberDetail().canSendSMS() ? Boolean.TRUE : "No Mobile No.";
		return new Object[] { Formats.presentableFlatNumber(getMemberDetail().getFlatNo()), pendingDetail.getAmount(), emailEntry, smsEntry };
	}

	public PendingDetail getPendingDetail() {
		return pendingDetail;
	}

	public void setPendingDetail(PendingDetail pendingDetail) {
		this.pendingDetail = pendingDetail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result
				+ ((pendingDetail.getPendingDate() == null) ? 0 : pendingDetail.getPendingDate().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean parentEquals = super.equals(obj);
		if (!parentEquals) {
			return false;
		}
		if (!(obj instanceof PendingNotification)) {
			return false;
		}
		PendingNotification other = (PendingNotification) obj;
		if (pendingDetail == null) {
			if (other.pendingDetail != null)
				return false;
		} else if (!pendingDetail.getPendingDate().equals(other.pendingDetail.getPendingDate()))
			return false;
		return true;
	}

	@Override
	public int compareTo(PendingNotification o) {
		int superComp = notificationCompareTo(o);
		if (superComp != 0) {
			return superComp;
		}
		return pendingDetail.getPendingDate().compareTo(o.getPendingDetail().getPendingDate());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Notification [").append(super.toString()).append(", pendingDetail=").append(pendingDetail)
				.append("]");
		return builder.toString();
	}

	@Override
	public Map<String, String> buildTypeTemplate() {
		String formattedAmount = Formats.AMOUNT_FORMATTER.format(pendingDetail.getAmount());
		String formattedPendingDate = null;
		if (pendingDetail.getPendingDate() != null) {
			formattedPendingDate = Formats.DATE_FORMAT.format(pendingDetail.getPendingDate());
		}
		Map<String, String> valueMap = new HashMap<>();
		valueMap.put(TEMPLATE_PENDING_AMOUNT, formattedAmount);
		valueMap.put(TEMPLATE_PENDING_DATE, formattedPendingDate);
		return valueMap;
	}

}

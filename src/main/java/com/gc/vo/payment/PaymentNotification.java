package com.gc.vo.payment;

import java.util.HashMap;
import java.util.Map;

import com.gc.util.Formats;
import com.gc.vo.Notification;

public class PaymentNotification extends Notification implements Comparable<PaymentNotification> {

	private static final String TEMPLATE_PAYMENT_AMOUNT = "payment-amount";

	private static final String TEMPLATE_CHEQUE_NO = "cheque-no";

	private static final String TEMPLATE_RECEIVED_DATE = "received-date";

	private static final String TEMPLATE_VOUCHER_NO = "voucher-no";

	public static final String[] HEADERS = { "Flat No.", "Voucher No", "Amount", "Date", "Send Email", "Send SMS" };

	private PaymentDetail paymentDetail;

	public Object[] getTableDataArray() {
		Object emailEntry = getMemberDetail().canSendEmail() ? Boolean.TRUE : "No Email ID";
		Object smsEntry = getMemberDetail().canSendSMS() ? Boolean.TRUE : "No Mobile No.";
		String paymentDate = null;
		if (paymentDetail.getPaymentDate() != null) {
			paymentDate = Formats.DATE_FORMAT.format(paymentDetail.getPaymentDate());
		}
		return new Object[] { Formats.presentableFlatNumber(getMemberDetail().getFlatNo()),
				paymentDetail.getVoucherNo(), paymentDetail.getAmount(), paymentDate, emailEntry, smsEntry };
	}

	public PaymentDetail getPaymentDetail() {
		return paymentDetail;
	}

	public void setPaymentDetail(PaymentDetail paymentDetail) {
		this.paymentDetail = paymentDetail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result
				+ ((paymentDetail.getPaymentDate() == null) ? 0 : paymentDetail.getPaymentDate().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean parentEquals = super.equals(obj);
		if (!parentEquals) {
			return false;
		}
		if (!(obj instanceof PaymentNotification)) {
			return false;
		}
		PaymentNotification other = (PaymentNotification) obj;
		if (paymentDetail == null) {
			if (other.paymentDetail != null)
				return false;
		} else if (!paymentDetail.getPaymentDate().equals(other.paymentDetail.getPaymentDate()))
			return false;
		return true;
	}

	@Override
	public int compareTo(PaymentNotification o) {
		int superComp = notificationCompareTo(o);
		if (superComp != 0) {
			return superComp;
		}
		return paymentDetail.getPaymentDate().compareTo(o.getPaymentDetail().getPaymentDate());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Notification [").append(super.toString()).append(", paymentDetail=").append(paymentDetail)
				.append("]");
		return builder.toString();
	}

	@Override
	public Map<String, String> buildTypeTemplate() {
		String formattedAmount = Formats.AMOUNT_FORMATTER.format(paymentDetail.getAmount());
		String formattedRecDate = null;
		if (paymentDetail.getPaymentDate() != null) {
			formattedRecDate = Formats.DATE_FORMAT.format(paymentDetail.getPaymentDate());
		}
		Map<String, String> valueMap = new HashMap<>();
		valueMap.put(TEMPLATE_CHEQUE_NO, paymentDetail.getCheckNo());
		valueMap.put(TEMPLATE_PAYMENT_AMOUNT, formattedAmount);
		valueMap.put(TEMPLATE_RECEIVED_DATE, formattedRecDate);
		valueMap.put(TEMPLATE_VOUCHER_NO, String.valueOf(paymentDetail.getVoucherNo()));
		return valueMap;
	}

}

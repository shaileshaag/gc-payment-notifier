package com.gc.vo;

import org.springframework.util.StringUtils;

import com.gc.util.Formats;

public class Notification implements Comparable<Notification> {

	public static final String[] HEADERS = { "Flat No.", "Voucher No", "Amount", "Date", "Send Email", "Send SMS" };

	private MemberDetail memberDetail;

	private PaymentDetail paymentDetail;

	private boolean sendEmail;

	private boolean sendSms;

	public Object[] getTableDataArray() {
		Object emailEntry = "No Email ID";
		Object smsEntry = "No Mobile No.";
		if (!StringUtils.isEmpty(memberDetail.getEmail())) {
			emailEntry = Boolean.TRUE;
		}
		if (!StringUtils.isEmpty(memberDetail.getMobile())) {
			smsEntry = Boolean.TRUE;
		}
		String paymentDate = null;
		if (paymentDetail.getPaymentDate() != null) {
			paymentDate = Formats.DATE_FORMAT.format(paymentDetail.getPaymentDate());
		}
		return new Object[] { memberDetail.getFlatNo(), paymentDetail.getVoucherNo(), paymentDetail.getAmount(), paymentDate, emailEntry,
				smsEntry };
	}

	public MemberDetail getMemberDetail() {
		return memberDetail;
	}

	public void setMemberDetail(MemberDetail memberDetail) {
		this.memberDetail = memberDetail;
	}

	public PaymentDetail getPaymentDetail() {
		return paymentDetail;
	}

	public void setPaymentDetail(PaymentDetail paymentDetail) {
		this.paymentDetail = paymentDetail;
	}

	public boolean isSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public boolean isSendSms() {
		return sendSms;
	}

	public void setSendSms(boolean sendSms) {
		this.sendSms = sendSms;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((memberDetail.getFlatNo() == null) ? 0 : memberDetail.getFlatNo().hashCode());
		result = prime * result
				+ ((paymentDetail.getPaymentDate() == null) ? 0 : paymentDetail.getPaymentDate().hashCode());
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
		Notification other = (Notification) obj;
		if (memberDetail == null) {
			if (other.memberDetail != null)
				return false;
		} else if (!memberDetail.getFlatNo().equals(other.memberDetail.getFlatNo()))
			return false;
		if (paymentDetail == null) {
			if (other.paymentDetail != null)
				return false;
		} else if (!paymentDetail.getPaymentDate().equals(other.paymentDetail.getPaymentDate()))
			return false;
		return true;
	}

	@Override
	public int compareTo(Notification o) {
		int flatNoComp = memberDetail.getFlatNo().compareTo(o.getMemberDetail().getFlatNo());
		if (flatNoComp != 0) {
			return flatNoComp;
		}
		return paymentDetail.getPaymentDate().compareTo(o.getPaymentDetail().getPaymentDate());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Notification [memberDetail=").append(memberDetail).append(", paymentDetail=")
				.append(paymentDetail).append(", sendEmail=").append(sendEmail).append(", sendSms=").append(sendSms)
				.append("]");
		return builder.toString();
	}

}

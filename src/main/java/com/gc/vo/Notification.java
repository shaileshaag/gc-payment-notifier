package com.gc.vo;

import java.util.HashMap;
import java.util.Map;

public abstract class Notification {

	private static final String TEMPLATE_FLAT_NO = "flat-no";

	private MemberDetail memberDetail;

	private boolean sendEmail;

	private boolean sendSms;

	public MemberDetail getMemberDetail() {
		return memberDetail;
	}

	public void setMemberDetail(MemberDetail memberDetail) {
		this.memberDetail = memberDetail;
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
		return true;
	}

	public int notificationCompareTo(Notification o) {
		return memberDetail.getFlatNo().compareTo(o.getMemberDetail().getFlatNo());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("memberDetail=").append(memberDetail).append(", sendEmail=").append(sendEmail)
				.append(", sendSms=").append(sendSms);
		return builder.toString();
	}

	public Map<String, String> getTemplate() {
		Map<String, String> valueMap = new HashMap<>();
		valueMap.put(TEMPLATE_FLAT_NO, memberDetail.getFlatNo());
		Map<String, String> typeTemplate = buildTypeTemplate();
		if (typeTemplate != null) {
			valueMap.putAll(typeTemplate);
		}
		return valueMap;
	}

	public abstract Object[] getTableDataArray();

	protected abstract Map<String, String> buildTypeTemplate();

}

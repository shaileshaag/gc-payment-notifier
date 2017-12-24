package com.gc.vo.member;

import org.apache.commons.lang3.StringUtils;

public class MemberDetail {

	private String flatNo;

	private String mobile;

	private String email;

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean canSendEmail() {
		return StringUtils.isNotBlank(email);
	}

	public boolean canSendSMS() {
		return StringUtils.isNotBlank(mobile);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MemberDetail [flatNo=").append(flatNo).append(", mobile=").append(mobile).append(", email=")
				.append(email).append("]");
		return builder.toString();
	}

}

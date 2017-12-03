package com.gc.vo;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MemberDetail [flatNo=").append(flatNo).append(", mobile=").append(mobile).append(", email=")
				.append(email).append("]");
		return builder.toString();
	}

}

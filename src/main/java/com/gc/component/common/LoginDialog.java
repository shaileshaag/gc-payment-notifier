package com.gc.component.common;

public interface LoginDialog {

	String SEND = "Send";

	String CANCEL = "Cancel";

	String getEmailUsername();

	String getEmailPassword();

	String getSmsUsername();

	String getSmsPassword();

	boolean isSucceeded();

}

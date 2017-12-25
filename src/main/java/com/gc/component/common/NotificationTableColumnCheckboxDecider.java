package com.gc.component.common;

public interface NotificationTableColumnCheckboxDecider {

	boolean isCheckBox(int column);

	int getEmailColumnNumber();

	int getSmsColumnNumber();

}

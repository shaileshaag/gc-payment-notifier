package com.gc.component.common;

public abstract class NotificationTableColumnCheckboxDecider {

	public boolean isCheckBox(int column) {
		return (column == getEmailColumnNumber() || column == getSmsColumnNumber());
	}

	public abstract int getEmailColumnNumber();

	public abstract int getSmsColumnNumber();

}

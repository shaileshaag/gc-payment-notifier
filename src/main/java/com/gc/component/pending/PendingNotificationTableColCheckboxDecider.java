package com.gc.component.pending;

import com.gc.component.common.NotificationTableColumnCheckboxDecider;

public class PendingNotificationTableColCheckboxDecider implements NotificationTableColumnCheckboxDecider {

	@Override
	public boolean isCheckBox(int column) {
		return (column == getEmailColumnNumber() || column == getSmsColumnNumber());
	}

	@Override
	public int getEmailColumnNumber() {
		return 2;
	}

	@Override
	public int getSmsColumnNumber() {
		return 3;
	}

}

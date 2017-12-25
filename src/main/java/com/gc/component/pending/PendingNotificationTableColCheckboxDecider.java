package com.gc.component.pending;

import com.gc.component.common.NotificationTableColumnCheckboxDecider;

public class PendingNotificationTableColCheckboxDecider extends NotificationTableColumnCheckboxDecider {

	@Override
	public int getEmailColumnNumber() {
		return 2;
	}

	@Override
	public int getSmsColumnNumber() {
		return 3;
	}

}

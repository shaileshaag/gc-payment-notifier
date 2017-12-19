package com.gc.component.pending;

import com.gc.component.common.NotificationTableColumnCheckboxDecider;

public class PendingNotificationTableColCheckboxDecider implements NotificationTableColumnCheckboxDecider {

	@Override
	public boolean isCheckBox(int column) {
		return (column == 2 || column == 3);
	}

}

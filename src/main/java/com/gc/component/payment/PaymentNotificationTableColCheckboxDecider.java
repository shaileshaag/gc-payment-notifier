package com.gc.component.payment;

import com.gc.component.common.NotificationTableColumnCheckboxDecider;

public class PaymentNotificationTableColCheckboxDecider implements NotificationTableColumnCheckboxDecider {

	@Override
	public boolean isCheckBox(int column) {
		return (column == 4 || column == 5);
	}

}

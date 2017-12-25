package com.gc.component.payment;

import com.gc.component.common.NotificationTableColumnCheckboxDecider;

public class PaymentNotificationTableColCheckboxDecider implements NotificationTableColumnCheckboxDecider {

	@Override
	public boolean isCheckBox(int column) {
		return (column == 4 || column == 5);
	}

	@Override
	public int getEmailColumnNumber() {
		return 4;
	}

	@Override
	public int getSmsColumnNumber() {
		return 5;
	}

}

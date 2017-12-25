package com.gc.component.payment;

import com.gc.component.common.NotificationTableColumnCheckboxDecider;

public class PaymentNotificationTableColCheckboxDecider implements NotificationTableColumnCheckboxDecider {

	@Override
	public boolean isCheckBox(int column) {
		return (column == getEmailColumnNumber() || column == getSmsColumnNumber());
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

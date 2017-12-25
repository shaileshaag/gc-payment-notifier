package com.gc.component.payment;

import com.gc.component.common.NotificationTableColumnCheckboxDecider;

public class PaymentNotificationTableColCheckboxDecider extends NotificationTableColumnCheckboxDecider {

	@Override
	public int getEmailColumnNumber() {
		return 4;
	}

	@Override
	public int getSmsColumnNumber() {
		return 5;
	}

}

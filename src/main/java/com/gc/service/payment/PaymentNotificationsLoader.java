package com.gc.service.payment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.gc.provider.DateProvider;
import com.gc.provider.FileProvider;
import com.gc.provider.NotificationReceiver;
import com.gc.service.member.MemberDetailsReader;
import com.gc.vo.member.MemberDetail;
import com.gc.vo.payment.PaymentDetail;
import com.gc.vo.payment.PaymentNotification;

public class PaymentNotificationsLoader {

	private final FileProvider memberDetailsFilePanel;

	private final FileProvider memberPaymentsFilePanel;

	private final DateProvider fromDatePickerPanel;

	private final DateProvider toDatePickerPanel;

	private final NotificationReceiver notificationPanel;

	private final MemberDetailsReader memberDetailsReader;

	private final PaymentDetailsReader paymentDetailsReader;

	public PaymentNotificationsLoader(FileProvider memberDetailsFilePanel, FileProvider memberPaymentsFilePanel,
			DateProvider fromDatePickerPanel, DateProvider toDatePickerPanel, NotificationReceiver notificationPanel,
			MemberDetailsReader memberDetailsReader, PaymentDetailsReader paymentDetailsReader) {
		this.memberDetailsFilePanel = memberDetailsFilePanel;
		this.memberPaymentsFilePanel = memberPaymentsFilePanel;
		this.fromDatePickerPanel = fromDatePickerPanel;
		this.toDatePickerPanel = toDatePickerPanel;
		this.notificationPanel = notificationPanel;
		this.memberDetailsReader = memberDetailsReader;
		this.paymentDetailsReader = paymentDetailsReader;
	}

	public void load() throws FileNotFoundException, IOException {
		File memberDetailsFile = memberDetailsFilePanel.getFile();
		File memberPaymentsFile = memberPaymentsFilePanel.getFile();
		Date fromDate = fromDatePickerPanel.getDate();
		Date toDate = toDatePickerPanel.getDate();
		if (toDate == null) {
			Calendar calInst = Calendar.getInstance();
			calInst.set(Calendar.HOUR_OF_DAY, 23);
			calInst.set(Calendar.MINUTE, 59);
			calInst.set(Calendar.SECOND, 59);
			calInst.set(Calendar.MILLISECOND, 999);
			toDate = calInst.getTime();
		}
		validateDetails(memberDetailsFile, memberPaymentsFile, fromDate);
		List<MemberDetail> members = memberDetailsReader.read(memberDetailsFile);
		Map<String, List<PaymentDetail>> paymentDetails = paymentDetailsReader.read(memberPaymentsFile);
		List<PaymentNotification> notifications = buildNotifications(members, paymentDetails, fromDate, toDate);
		notificationPanel.receive(notifications);
	}

	private List<PaymentNotification> buildNotifications(List<MemberDetail> members,
			Map<String, List<PaymentDetail>> paymentDetails, Date fromDate, Date toDate) {
		List<PaymentNotification> notifications = new ArrayList<>();
		for (MemberDetail m : members) {
			String flatNo = m.getFlatNo();
			if (!paymentDetails.containsKey(flatNo)) {
				continue;
			}
			List<PaymentDetail> payments = paymentDetails.get(flatNo);
			for (PaymentDetail pd : payments) {
				if (pd != null) {
					if ((pd.getPaymentDate() != null
							&& (pd.getPaymentDate().equals(fromDate) || pd.getPaymentDate().after(fromDate)))
							&& (pd.getPaymentDate().equals(toDate) || pd.getPaymentDate().before(toDate))) {
						PaymentNotification n = new PaymentNotification();
						n.setMemberDetail(m);
						n.setPaymentDetail(pd);
						n.setSendEmail(!StringUtils.isEmpty(m.getEmail()));
						n.setSendSms(!StringUtils.isEmpty(m.getMobile()));
						notifications.add(n);
					}
				}
			}
		}
		Collections.sort(notifications);
		return notifications;
	}

	private void validateDetails(File memberDetailsFile, File memberPaymentsFile, Date fromDate) {
		if (memberDetailsFile == null) {
			throw new IllegalArgumentException("No member details file selected");
		} else if (memberPaymentsFile == null) {
			throw new IllegalArgumentException("No member payments file selected");
		} else if (fromDate == null) {
			throw new IllegalArgumentException("No from date selected");
		}
	}

}

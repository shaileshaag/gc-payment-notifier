package com.gc.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.gc.provider.DateProvider;
import com.gc.provider.FileProvider;
import com.gc.provider.NotificationReceiver;
import com.gc.vo.MemberDetail;
import com.gc.vo.Notification;
import com.gc.vo.PaymentDetail;

public class NotificationsLoader {

	private final FileProvider memberDetailsFilePanel;

	private final FileProvider memberPaymentsFilePanel;

	private final DateProvider fromDatePickerPanel;

	private final NotificationReceiver notificationPanel;

	private final MemberDetailsReader memberDetailsReader;

	private final PaymentDetailsReader paymentDetailsReader;

	public NotificationsLoader(FileProvider memberDetailsFilePanel, FileProvider memberPaymentsFilePanel,
			DateProvider fromDatePickerPanel, NotificationReceiver notificationPanel,
			MemberDetailsReader memberDetailsReader, PaymentDetailsReader paymentDetailsReader) {
		this.memberDetailsFilePanel = memberDetailsFilePanel;
		this.memberPaymentsFilePanel = memberPaymentsFilePanel;
		this.fromDatePickerPanel = fromDatePickerPanel;
		this.notificationPanel = notificationPanel;
		this.memberDetailsReader = memberDetailsReader;
		this.paymentDetailsReader = paymentDetailsReader;
	}

	public void load() throws FileNotFoundException, IOException {
		File memberDetailsFile = memberDetailsFilePanel.getFile();
		File memberPaymentsFile = memberPaymentsFilePanel.getFile();
		Date fromDate = fromDatePickerPanel.getDate();
		validateDetails(memberDetailsFile, memberPaymentsFile, fromDate);
		List<MemberDetail> members = memberDetailsReader.read(memberDetailsFile);
		Map<String, List<PaymentDetail>> paymentDetails = paymentDetailsReader.read(memberPaymentsFile);
		List<Notification> notifications = buildNotifications(members, paymentDetails, fromDate);
		notificationPanel.receive(notifications);
	}

	private List<Notification> buildNotifications(List<MemberDetail> members,
			Map<String, List<PaymentDetail>> paymentDetails, Date fromDate) {
		List<Notification> notifications = new ArrayList<>();
		for (MemberDetail m : members) {
			String flatNo = m.getFlatNo();
			if (!paymentDetails.containsKey(flatNo)) {
				continue;
			}
			List<PaymentDetail> payments = paymentDetails.get(flatNo);
			for (PaymentDetail pd : payments) {
				if (pd != null) {
					if ((pd.getPaymentDate() != null
							&& (pd.getPaymentDate().equals(fromDate) || pd.getPaymentDate().after(fromDate)))) {
						Notification n = new Notification();
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
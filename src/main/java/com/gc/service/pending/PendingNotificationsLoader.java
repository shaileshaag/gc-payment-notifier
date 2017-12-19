package com.gc.service.pending;

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
import com.gc.service.MemberDetailsReader;
import com.gc.vo.MemberDetail;
import com.gc.vo.pending.PendingDetail;
import com.gc.vo.pending.PendingNotification;

public class PendingNotificationsLoader {

	private final FileProvider memberDetailsFilePanel;

	private final FileProvider memberPendingFilePanel;

	private final DateProvider pendingDatePickerPanel;

	private final NotificationReceiver notificationPanel;

	private final MemberDetailsReader memberDetailsReader;

	private final PendingDetailsReader pendingDetailsReader;

	public PendingNotificationsLoader(FileProvider memberDetailsFilePanel, FileProvider memberPendingFilePanel,
			DateProvider pendingDatePickerPanel, NotificationReceiver notificationPanel,
			MemberDetailsReader memberDetailsReader, PendingDetailsReader pendingDetailsReader) {
		this.memberDetailsFilePanel = memberDetailsFilePanel;
		this.memberPendingFilePanel = memberPendingFilePanel;
		this.pendingDatePickerPanel = pendingDatePickerPanel;
		this.notificationPanel = notificationPanel;
		this.memberDetailsReader = memberDetailsReader;
		this.pendingDetailsReader = pendingDetailsReader;
	}

	public void load() throws FileNotFoundException, IOException {
		File memberDetailsFile = memberDetailsFilePanel.getFile();
		File memberPaymentsFile = memberPendingFilePanel.getFile();
		Date pendingDate = pendingDatePickerPanel.getDate();
		validateDetails(memberDetailsFile, memberPaymentsFile, pendingDate);
		List<MemberDetail> members = memberDetailsReader.read(memberDetailsFile);
		Map<String, List<PendingDetail>> pendingDetails = pendingDetailsReader.read(memberPaymentsFile, pendingDate);
		List<PendingNotification> notifications = buildNotifications(members, pendingDetails, pendingDate);
		notificationPanel.receive(notifications);
	}

	private List<PendingNotification> buildNotifications(List<MemberDetail> members,
			Map<String, List<PendingDetail>> pendingDetails, Date fromDate) {
		List<PendingNotification> notifications = new ArrayList<>();
		for (MemberDetail m : members) {
			String flatNo = m.getFlatNo();
			if (!pendingDetails.containsKey(flatNo)) {
				continue;
			}
			List<PendingDetail> payments = pendingDetails.get(flatNo);
			for (PendingDetail pd : payments) {
				if (pd != null) {
					if ((pd.getPendingDate() != null
							&& (pd.getPendingDate().equals(fromDate) || pd.getPendingDate().after(fromDate)))) {
						PendingNotification n = new PendingNotification();
						n.setMemberDetail(m);
						n.setPendingDetail(pd);
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
			throw new IllegalArgumentException("No member pending payments file selected");
		} else if (fromDate == null) {
			throw new IllegalArgumentException("Pending date not selected");
		}
	}

}

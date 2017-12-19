package com.gc.component.pending;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.component.common.ComponentGroupPanel;
import com.gc.component.common.NotificationTab;
import com.gc.service.pending.PendingNotificationsLoader;

public class PendingNotificationTab extends NotificationTab implements ActionListener {

	private static final long serialVersionUID = -6672741297733662373L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PendingNotificationTab.class);

	private final ComponentGroupPanel memberPendingFilePanel;

	private final ComponentGroupPanel pendingDatePickerPanel;

	private final ComponentGroupPanel notificationPanel;

	private final PendingNotificationsLoader notificationsLoader;

	public PendingNotificationTab(ComponentGroupPanel memberPendingFilePanel,
			ComponentGroupPanel pendingDatePickerPanel, ComponentGroupPanel notificationPanel,
			PendingNotificationsLoader notificationsLoader) {
		super();
		this.memberPendingFilePanel = memberPendingFilePanel;
		this.pendingDatePickerPanel = pendingDatePickerPanel;
		this.notificationPanel = notificationPanel;
		this.notificationsLoader = notificationsLoader;
	}

	public void init() {
		GroupLayout tabbedGroupLayout = new GroupLayout(this);
		setLayout(tabbedGroupLayout);

		JButton loadNotificationsButton = new JButton("Load Pending Notifications");
		loadNotificationsButton.addActionListener(this);

		tabbedGroupLayout.setHorizontalGroup(tabbedGroupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(memberPendingFilePanel.getHorizontalComponents(tabbedGroupLayout))
				.addGroup(pendingDatePickerPanel.getHorizontalComponents(tabbedGroupLayout))
				.addGroup(tabbedGroupLayout.createSequentialGroup().addGap(30).addComponent(loadNotificationsButton))
				.addGroup(notificationPanel.getHorizontalComponents(tabbedGroupLayout)));
		tabbedGroupLayout.setVerticalGroup(tabbedGroupLayout.createSequentialGroup()
				.addGroup(memberPendingFilePanel.getVerticalComponents(tabbedGroupLayout)).addGap(10)
				.addGroup(pendingDatePickerPanel.getVerticalComponents(tabbedGroupLayout)).addGap(10)
				.addComponent(loadNotificationsButton).addGap(10)
				.addGroup(notificationPanel.getVerticalComponents(tabbedGroupLayout)));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			notificationsLoader.load();
		} catch (IllegalArgumentException | IOException iae) {
			LOGGER.error("Error while loading notifications", iae);
			JOptionPane.showMessageDialog(this, iae.getMessage(), "Pending Notifications Load Warning",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public String getTabName() {
		return "Pending Notifications";
	}

	@Override
	public void setParentFrame(JFrame parentFrame) {
		memberPendingFilePanel.setParentFrame(parentFrame);
		pendingDatePickerPanel.setParentFrame(parentFrame);
		notificationPanel.setParentFrame(parentFrame);
	}

}

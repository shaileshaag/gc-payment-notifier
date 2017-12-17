package com.gc.component.payment;

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
import com.gc.service.PaymentNotificationsLoader;

public class PaymentNotificationTab extends NotificationTab implements ActionListener {

	private static final long serialVersionUID = 3663606888456891876L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentNotificationTab.class);

	private final ComponentGroupPanel memberPaymentsFilePanel;

	private final ComponentGroupPanel fromDatePickerPanel;

	private final ComponentGroupPanel notificationPanel;

	private final PaymentNotificationsLoader notificationsLoader;

	public PaymentNotificationTab(ComponentGroupPanel memberPaymentsFilePanel, ComponentGroupPanel fromDatePickerPanel,
			ComponentGroupPanel notificationPanel, PaymentNotificationsLoader notificationsLoader) {
		super();
		this.memberPaymentsFilePanel = memberPaymentsFilePanel;
		this.fromDatePickerPanel = fromDatePickerPanel;
		this.notificationPanel = notificationPanel;
		this.notificationsLoader = notificationsLoader;
	}

	public void init() {
		GroupLayout tabbedGroupLayout = new GroupLayout(this);
		setLayout(tabbedGroupLayout);

		JButton loadNotificationsButton = new JButton("Load Notifications");
		loadNotificationsButton.addActionListener(this);

		tabbedGroupLayout.setHorizontalGroup(tabbedGroupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(memberPaymentsFilePanel.getHorizontalComponents(tabbedGroupLayout))
				.addGroup(fromDatePickerPanel.getHorizontalComponents(tabbedGroupLayout))
				.addGroup(tabbedGroupLayout.createSequentialGroup().addGap(30).addComponent(loadNotificationsButton))
				.addGroup(notificationPanel.getHorizontalComponents(tabbedGroupLayout)));
		tabbedGroupLayout.setVerticalGroup(tabbedGroupLayout.createSequentialGroup()
				.addGroup(memberPaymentsFilePanel.getVerticalComponents(tabbedGroupLayout)).addGap(10)
				.addGroup(fromDatePickerPanel.getVerticalComponents(tabbedGroupLayout)).addGap(10)
				.addComponent(loadNotificationsButton).addGap(10)
				.addGroup(notificationPanel.getVerticalComponents(tabbedGroupLayout)));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			notificationsLoader.load();
		} catch (IllegalArgumentException | IOException iae) {
			LOGGER.error("Error while loading notifications", iae);
			JOptionPane.showMessageDialog(this, iae.getMessage(), "Payment Notifications Load Warning",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public String getTabName() {
		return "Payment Notifications";
	}

	@Override
	public void setParentFrame(JFrame parentFrame) {
		memberPaymentsFilePanel.setParentFrame(parentFrame);
		fromDatePickerPanel.setParentFrame(parentFrame);
		notificationPanel.setParentFrame(parentFrame);
	}

}

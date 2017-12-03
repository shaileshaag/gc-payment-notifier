package com.gc.component;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.service.NotificationsLoader;

public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MainFrame.class);

	private final ComponentGroupPanel memberDetailsFilePanel;

	private final ComponentGroupPanel memberPaymentsFilePanel;

	private final ComponentGroupPanel fromDatePickerPanel;

	private final ComponentGroupPanel notificationPanel;

	private final NotificationsLoader notificationsLoader;

	public MainFrame(ComponentGroupPanel memberDetailsFilePanel, ComponentGroupPanel memberPaymentsFilePanel,
			ComponentGroupPanel fromDatePickerPanel, ComponentGroupPanel notificationPanel, NotificationsLoader notificationsLoader) throws HeadlessException {
		super();
		this.memberDetailsFilePanel = memberDetailsFilePanel;
		this.memberPaymentsFilePanel = memberPaymentsFilePanel;
		this.fromDatePickerPanel = fromDatePickerPanel;
		this.notificationPanel = notificationPanel;
		this.notificationsLoader = notificationsLoader;
	}

	public void init() {
		EventQueue.invokeLater(() -> {
			setupParentForPanels();
			loadPanels();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			pack();
			setVisible(true);
		});
	}

	private void setupParentForPanels() {
		memberDetailsFilePanel.setParentFrame(this);
		memberDetailsFilePanel.setParentFrame(this);
		fromDatePickerPanel.setParentFrame(this);
		notificationPanel.setParentFrame(this);
	}

	private void loadPanels() {
		Container contentPanel = getContentPane();
		GroupLayout groupLayout = new GroupLayout(contentPanel);
		contentPanel.setLayout(groupLayout);

		JLabel headerLabel = new JLabel(
				"  Green County Co-operative Housing Society - Phase 1 (Send Payment Notifications)  ");
		headerLabel.setFont(new Font("Courier", Font.BOLD, 20));

		JButton loadNotificationsButton = new JButton("Load Notifications");
		loadNotificationsButton.addActionListener(this);

		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(headerLabel).addGroup(memberDetailsFilePanel.getHorizontalComponents(groupLayout))
				.addGroup(memberPaymentsFilePanel.getHorizontalComponents(groupLayout))
				.addGroup(fromDatePickerPanel.getHorizontalComponents(groupLayout))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(30)
						.addComponent(loadNotificationsButton))
				.addGroup(notificationPanel.getHorizontalComponents(groupLayout)));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addComponent(headerLabel).addGap(20)
				.addGroup(memberDetailsFilePanel.getVerticalComponents(groupLayout)).addGap(10)
				.addGroup(memberPaymentsFilePanel.getVerticalComponents(groupLayout)).addGap(10)
				.addGroup(fromDatePickerPanel.getVerticalComponents(groupLayout)).addGap(10)
				.addComponent(loadNotificationsButton).addGap(10)
				.addGroup(notificationPanel.getVerticalComponents(groupLayout)));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			notificationsLoader.load();
		} catch (IllegalArgumentException | IOException iae) {
			LOGGER.error("Error while loading notifications", iae);
			JOptionPane.showMessageDialog(this, iae.getMessage(), "Notifications Load Warning", JOptionPane.ERROR_MESSAGE);
		}
	}

}

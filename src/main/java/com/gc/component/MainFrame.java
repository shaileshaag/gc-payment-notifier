package com.gc.component;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import com.gc.component.common.ComponentGroupPanel;
import com.gc.component.common.NotificationTab;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final ComponentGroupPanel memberDetailsFilePanel;

	private final List<NotificationTab> notificationTabs;

	public MainFrame(ComponentGroupPanel memberDetailsFilePanel, List<NotificationTab> notificationTabs)
			throws HeadlessException {
		super();
		this.memberDetailsFilePanel = memberDetailsFilePanel;
		this.notificationTabs = notificationTabs;
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
	}

	private void loadPanels() {
		Container contentPanel = getContentPane();
		GroupLayout groupLayout = new GroupLayout(contentPanel);
		contentPanel.setLayout(groupLayout);

		JLabel headerLabel = new JLabel(
				"  Green County Co-operative Housing Society - Phase 1 (Send Payment Notifications)  ");
		headerLabel.setFont(new Font("Courier", Font.BOLD, 20));

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		for (NotificationTab nt : notificationTabs) {
			nt.setParentFrame(this);
			tabbedPane.addTab(nt.getTabName(), nt);
		}

		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup().addComponent(headerLabel)
				.addGroup(memberDetailsFilePanel.getHorizontalComponents(groupLayout)).addComponent(tabbedPane));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addComponent(headerLabel).addGap(20)
				.addGroup(memberDetailsFilePanel.getVerticalComponents(groupLayout)).addGap(10)
				.addComponent(tabbedPane));
	}

}

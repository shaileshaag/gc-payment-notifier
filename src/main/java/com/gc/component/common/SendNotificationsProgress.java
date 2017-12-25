package com.gc.component.common;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import com.gc.component.listener.ProgressListener;

public class SendNotificationsProgress extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private List<SendNotificationProgressListener> progressListeners;

	private JTextArea taskOutput;

	private JButton okBtn;

	public SendNotificationsProgress(JFrame parentFrame) {
		super(parentFrame, "Notification Progress", true);
		setLocationRelativeTo(parentFrame);
		setResizable(false);
		this.progressListeners = new ArrayList<>();
	}

	public void init() {
		taskOutput = new JTextArea(30, 50);
		taskOutput.setMargin(new Insets(5, 5, 5, 5));
		taskOutput.setEditable(false);
		((DefaultCaret) taskOutput.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		okBtn = new JButton("OK");
		okBtn.setEnabled(false);
		okBtn.addActionListener(this);
		getRootPane().setDefaultButton(okBtn);

		JScrollPane jsp = new JScrollPane(taskOutput);

		Container contentPanel = getContentPane();
		GroupLayout groupLayout = new GroupLayout(contentPanel);
		contentPanel.setLayout(groupLayout);

		ParallelGroup horizontalComp = groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
		horizontalComp.addComponent(jsp);
		for (SendNotificationProgressListener snpl : progressListeners) {
			horizontalComp.addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(snpl.jLabel).addGap(5)
					.addComponent(snpl.associatedProgressBar).addGap(10));
		}
		horizontalComp.addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(okBtn));

		SequentialGroup verticalComp = groupLayout.createSequentialGroup();
		verticalComp.addComponent(jsp);
		for (SendNotificationProgressListener snpl : progressListeners) {
			verticalComp.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(snpl.jLabel).addComponent(snpl.associatedProgressBar));
		}
		verticalComp.addComponent(okBtn);

		groupLayout.setHorizontalGroup(horizontalComp);
		groupLayout.setVerticalGroup(verticalComp);
		pack();
	}

	public ProgressListener createProgressListener(String label) {
		SendNotificationProgressListener spl = new SendNotificationProgressListener(label);
		progressListeners.add(spl);
		return spl;
	}

	private void notificationCompleted() {
		for (SendNotificationProgressListener snpl : progressListeners) {
			if (!snpl.completed) {
				return;
			}
		}
		okBtn.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}

	public void addProcessOnLoad(Runnable runnable) {
		addComponentListener(new StartProcessOnLoadListener(runnable));
	}

	private class SendNotificationProgressListener implements ProgressListener {

		private JProgressBar associatedProgressBar;

		private JLabel jLabel;

		private boolean completed;

		private int value;

		private SendNotificationProgressListener(String label) {
			this.jLabel = new JLabel(label);
			this.jLabel.setSize(new Dimension(30, 10));
			this.associatedProgressBar = new JProgressBar();
		}

		@Override
		public void setProgressMax(int totalNotifications) {
			associatedProgressBar.setMinimum(0);
			associatedProgressBar.setMaximum(totalNotifications);
			associatedProgressBar.setValue(0);
		}

		@Override
		public void sent(String sentNotification) {
			associatedProgressBar.setValue(++value);
			taskOutput.append(sentNotification);
			taskOutput.append("\n");
		}

		@Override
		public void intimate(String message) {
			taskOutput.append(message);
			taskOutput.append("\n");
		}

		@Override
		public void markComplete() {
			this.completed = true;
			notificationCompleted();
		}

	}

	private class StartProcessOnLoadListener implements ComponentListener {

		private Runnable runnableOnLoad;

		private StartProcessOnLoadListener(Runnable runnableOnLoad) {
			this.runnableOnLoad = runnableOnLoad;
		}

		@Override
		public void componentResized(ComponentEvent e) {
		}

		@Override
		public void componentMoved(ComponentEvent e) {
		}

		@Override
		public void componentShown(ComponentEvent e) {
			new Thread(runnableOnLoad).start();
		}

		@Override
		public void componentHidden(ComponentEvent e) {
		}

	}

}

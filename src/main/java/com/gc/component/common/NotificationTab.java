package com.gc.component.common;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class NotificationTab extends JPanel {

	private static final long serialVersionUID = 1L;

	public abstract String getTabName();

	public abstract void setParentFrame(JFrame parentFrame);

}

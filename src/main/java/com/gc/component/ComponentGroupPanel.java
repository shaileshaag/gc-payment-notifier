package com.gc.component;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JFrame;

public interface ComponentGroupPanel {

	void setParentFrame(JFrame parentFrame);

	Group getVerticalComponents(GroupLayout groupLayout);

	Group getHorizontalComponents(GroupLayout groupLayout);

}

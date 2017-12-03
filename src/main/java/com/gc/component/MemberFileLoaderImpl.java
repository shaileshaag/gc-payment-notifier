package com.gc.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.gc.provider.FileProvider;

public class MemberFileLoaderImpl implements FileProvider, ComponentGroupPanel, ActionListener {

	private final String labelName;

	private final String buttonText;

	private File file;

	private JLabel fileNameLabel;

	private JButton fileLoadButton;

	private JLabel fileLabel;

	private JFileChooser fileChooser;

	private JFrame parentFrame;

	public MemberFileLoaderImpl(String labelName, String buttonText) {
		this.labelName = labelName;
		this.buttonText = buttonText;
	}

	public void init() {
		fileNameLabel = new JLabel(labelName);
		fileLoadButton = new JButton(buttonText);
		fileLoadButton.addActionListener(this);
		fileLabel = new JLabel();
		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("xls Files", "xls");
		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public Group getVerticalComponents(GroupLayout groupLayout) {
		return groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(fileNameLabel)
				.addComponent(fileLoadButton).addComponent(fileLabel);
	}

	@Override
	public Group getHorizontalComponents(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addGap(30).addComponent(fileNameLabel).addGap(10, 20, 100)
				.addComponent(fileLoadButton).addGap(10, 20, 100).addComponent(fileLabel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == fileLoadButton) {
			int fileChooserSelected = fileChooser.showDialog(parentFrame, "Choose");
			if (fileChooserSelected == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				fileLabel.setText(file.getAbsolutePath());
			}
		}
	}

	@Override
	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

}

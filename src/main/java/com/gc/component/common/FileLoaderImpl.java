package com.gc.component.common;

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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.provider.FileProvider;

public class FileLoaderImpl implements FileProvider, ComponentGroupPanel, ActionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileLoaderImpl.class);

	private final String labelName;

	private final String buttonText;

	private File file;

	private JLabel fileNameLabel;

	private JButton fileLoadButton;

	private JLabel fileLabel;

	private JFileChooser fileChooser;

	private JFrame parentFrame;

	public FileLoaderImpl(String labelName, String buttonText) {
		this(labelName, buttonText, null);
	}

	public FileLoaderImpl(String labelName, String buttonText, String defaultPath) {
		this.labelName = labelName;
		this.buttonText = buttonText;
		if (StringUtils.isNotBlank(defaultPath)) {
			LOGGER.info("Trying to load default file path '{}' for label '{}'", defaultPath, labelName);
			File defaultFile = new File(defaultPath);
			if (defaultFile.exists() && defaultFile.isFile()) {
				file = defaultFile;
				LOGGER.info("Default File '{}' found for label '{}'", defaultFile.getAbsolutePath(), labelName);
			} else {
				LOGGER.warn("Default file '{}' not found for label '{}'", defaultFile.getAbsolutePath(), labelName);
			}
		}
	}

	public void init() {
		fileNameLabel = new JLabel(labelName);
		fileLoadButton = new JButton(buttonText);
		fileLoadButton.addActionListener(this);
		fileLabel = null;
		if (file == null) {
			fileLabel = new JLabel();
		} else {
			fileLabel = new JLabel(file.getAbsolutePath());
		}
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

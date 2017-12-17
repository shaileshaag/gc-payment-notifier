package com.gc.component.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jdatepicker.DateModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.springframework.util.StringUtils;

import com.gc.provider.DateProvider;

public class DatePickerPanel implements ComponentGroupPanel, DateProvider {

	private final DateFormat dateFormat;

	private final String labelName;

	private JDatePickerImpl datePicker;

	private JLabel datePickerLabel;

	private JLabel emptyLabel;

	private JFrame parentFrame;

	public DatePickerPanel(String datePattern, String labelName) {
		dateFormat = new SimpleDateFormat(datePattern);
		this.labelName = labelName;
	}

	public void init() {
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePickerLabel = new JLabel(labelName);
		emptyLabel = new JLabel(
				"                                                                                              ");
	}

	@Override
	public Group getVerticalComponents(GroupLayout groupLayout) {
		return groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(datePickerLabel)
				.addComponent(datePicker).addComponent(emptyLabel);
	}

	@Override
	public Group getHorizontalComponents(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addGap(30).addComponent(datePickerLabel).addGap(10, 20, 100)
				.addComponent(datePicker).addComponent(emptyLabel);
	}

	@Override
	public Date getDate() {
		if (StringUtils.isEmpty(datePicker.getJFormattedTextField().getText())) {
			return null;
		}
		Calendar c = GregorianCalendar.getInstance();
		DateModel<?> model = datePicker.getModel();
		c.set(Calendar.YEAR, model.getYear());
		c.set(Calendar.MONTH, model.getMonth());
		c.set(Calendar.DATE, model.getDay());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	private class DateLabelFormatter extends AbstractFormatter {

		private static final long serialVersionUID = 1L;

		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormat.parseObject(text);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
				Calendar cal = (Calendar) value;
				return dateFormat.format(cal.getTime());
			}
			return "";
		}

	}

	@Override
	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

}

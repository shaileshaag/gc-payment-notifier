package com.gc.component.common;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class NotificationTable extends JTable {

	private static final long serialVersionUID = 1L;

	private final NotificationTableColumnCheckboxDecider checkboxDecider;

	private Class<?> editingClass;

	public NotificationTable(NotificationTableColumnCheckboxDecider checkboxDecider, final Object[][] notificationsData,
			Object[] headers) {
		super(notificationsData, headers);
		putClientProperty("terminateEditOnFocusLost", true);
		setGridColor(Color.BLACK);
		this.checkboxDecider = checkboxDecider;
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		if (checkboxDecider.isCheckBox(modelColumn)) {
			Class<?> rowClass = getModel().getValueAt(row, modelColumn).getClass();
			return getDefaultRenderer(rowClass);
		} else {
			return super.getCellRenderer(row, column);
		}
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		if (checkboxDecider.isCheckBox(modelColumn)) {
			editingClass = getModel().getValueAt(row, modelColumn).getClass();
			return getDefaultEditor(editingClass);
		} else {
			return super.getCellEditor(row, column);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		if (checkboxDecider.isCheckBox(modelColumn)) {
			editingClass = getModel().getValueAt(row, modelColumn).getClass();
			if (editingClass == Boolean.class) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return editingClass != null ? editingClass : super.getColumnClass(column);
	}

}

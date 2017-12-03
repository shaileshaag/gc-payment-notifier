package com.gc.component;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class PaymentTable extends JTable {

	private static final long serialVersionUID = 1L;

	private Class editingClass;

	public PaymentTable(final Object[][] notificationsData, Object[] headers) {
		super(notificationsData, headers);
		putClientProperty("terminateEditOnFocusLost", true);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		if (isColumnCheckBox(modelColumn)) {
			Class rowClass = getModel().getValueAt(row, modelColumn).getClass();
			return getDefaultRenderer(rowClass);
		} else {
			return super.getCellRenderer(row, column);
		}
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		if (isColumnCheckBox(modelColumn)) {
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
		if (isColumnCheckBox(modelColumn)) {
			editingClass = getModel().getValueAt(row, modelColumn).getClass();
			if (editingClass == Boolean.class) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Class getColumnClass(int column) {
		return editingClass != null ? editingClass : super.getColumnClass(column);
	}

	private boolean isColumnCheckBox(int column) {
		return (column == 4 || column == 5);
	}

}

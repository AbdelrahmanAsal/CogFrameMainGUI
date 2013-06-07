package AttributePanel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class PacketsColorTableModel extends AbstractTableModel {
	String[] colNames = { "Type", "Color", "Show" };
	public ArrayList<PacketsColorRowEntry> current;
	protected int columnsCount = colNames.length;

	public PacketsColorTableModel() {
		current = new ArrayList<PacketsColorRowEntry>();
	}

	public int getRowCount() {
		return current == null ? 0 : current.size();
	}

	public int getColumnCount() {
		return columnsCount;
	}

	public String getColumnName(int column) {
		return colNames[column];
	}

	public Class getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		default:
			return Boolean.class;
		}
	}

	public boolean isCellEditable(int nRow, int nCol) {
		if (nCol == 2)
			return true;
		return false;
	}

	public Object getValueAt(int nRow, int nCol) {
		if (nRow < 0 || nRow >= current.size())
			return "";
		if (nCol == 0)
			return current.get(nRow).type;
		if (nCol == 2) 
			return current.get(nRow).show;
		return "";
	}

	public void setValueAt(Object value, int row, int col) {
		current.get(row).show = (Boolean) value;
		fireTableCellUpdated(row, col);
	}

	public String getTitle() {
		return "Data";
	}
}

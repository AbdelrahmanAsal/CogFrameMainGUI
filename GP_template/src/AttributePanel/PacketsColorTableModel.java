package AttributePanel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class PacketsColorTableModel extends AbstractTableModel {
	String[] colNames = { "Type", "Color" };
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

	public boolean isCellEditable(int nRow, int nCol) {
		return false;
	}

	public Object getValueAt(int nRow, int nCol) {
		if (nRow < 0 || nRow >= current.size())
			return "";
		if (nCol == 0)
			return current.get(nRow).type;
		return "";
	}

	public String getTitle() {
		return "Data";
	}
}

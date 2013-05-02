package AttributePanel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class WirelessInterfacesTable extends AbstractTableModel {
	String[] colNames = { "Name", "H/W" };
	ArrayList<WirelessTableRowEntry> current;
	protected int columnsCount = colNames.length;

	public WirelessInterfacesTable() {
		current = new ArrayList<WirelessTableRowEntry>();
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
			return current.get(nRow).interfaceName;
		if (nCol == 1)
			return current.get(nRow).interfaceHW;
		return "";
	}

	public String getTitle() {
		return "Data";
	}
}

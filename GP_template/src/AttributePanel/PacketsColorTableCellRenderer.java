package AttributePanel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PacketsColorTableCellRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component rendererComp = super.getTableCellRendererComponent(table,
				value, isSelected, hasFocus, row, column);
		PacketsColorTableModel tableModel = (PacketsColorTableModel) table.getModel();
		if (column == 1) {
			rendererComp.setBackground(tableModel.current.get(row).color);
		}
		return rendererComp;
	}
}

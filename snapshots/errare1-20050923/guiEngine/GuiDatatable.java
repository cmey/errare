package guiEngine;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import databaseEngine.DatabaseEngine;

public class GuiDatatable extends JPanel implements TableModelListener {
	//Component of the JFrame
	private JTable table;
	private DefaultTableModel tableModel;
	private String name;
	private Vector primkeys;
	
	public GuiDatatable(String tableName, DatabaseEngine db) throws SQLException {
		this.name = tableName;
		setLayout(new BorderLayout());
		
		
		tableModel = new DefaultTableModel();
		tableModel.addTableModelListener(this);
		
		ResultSet res = db.sendRequest("select * from "+tableName);
		ResultSetMetaData colums = res.getMetaData();
		int numColums = colums.getColumnCount();
		for(int i=1; i<=numColums; i++) {
			tableModel.addColumn(colums.getColumnLabel(i));
			
		}
		
		
		
		while(res.next()) {
			Object[] row = new Object[numColums];
			for(int i=0; i<numColums; i++) {
				row[i] = res.getObject(i+1);
			}
			
			tableModel.addRow(row);
		}
		
		
		
		
		table = new JTable(tableModel);
		table.setAutoscrolls(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setPreferredScrollableViewportSize(new Dimension(200,200));
		
		
		
		
		JScrollPane sp=new JScrollPane(table);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		add(sp, BorderLayout.CENTER);
		
	}


	public void tableChanged(TableModelEvent e) {
		if(table!=null) {
			int row = e.getFirstRow();
	        int column = e.getColumn();
	        String columnName = tableModel.getColumnName(column);
	        Object data = tableModel.getValueAt(row, column);
	        System.out.println(data);
		}
		
		
	}

	

}

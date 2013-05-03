/**
 * Programmers: Chase McCowan & Ed Broxson 
 * Date: 03/13/2013 
 * Modified 5-1-13 by Ed Broxson
 * Purpose: Make a table model for use in FreezePointDepression
 */
package chemistry;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import javax.swing.event.TableModelListener;

public class MyTableModel extends DefaultTableModel {    
  public MyTableModel() {
      JTable table = null;
      table.getModel().addTableModelListener((TableModelListener) this);
  }

  /** Construct a table model with specified data and columnNames */
  public MyTableModel(Object[][] data, Object[] columnNames) {
    super(data, columnNames);
  }

  /** Override this method to return a class for the column */
  @Override
  public Class getColumnClass(int column) {
    return getValueAt(0, column).getClass();
  }

  /** Override this method to return true if cell is editable */
  @Override
  public boolean isCellEditable(int row, int column) {
      //set columns and rows no to be allowed to be editable
      if (column < 2){  
        return false;  
      }
      if (row < 1 || row > 2){
          return false;
      }
      
     Class columnClass = getColumnClass(column);
     return columnClass != ImageIcon.class &&
      columnClass != Date.class;
  }
}

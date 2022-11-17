package sa.edu.sa.librarian.robot.gui.model;

import lejos.pc.comm.*;
import javax.swing.table.AbstractTableModel;

public class NXTConnectionTabelModel extends AbstractTableModel {

	private static final long serialVersionUID = 3540880662561527501L;
	
	private static final String[] columnNames = { "Name", "Protocol", "Address", "Status" };
	private static final int NUM_COLUMNS = 4;
	private Object[][] nxtData;
	private int numNXTs;

	/**
	 * Create the model from an array of NXTInfo. *
	 * 
	 * @param nxts the NXTInfo array
	 */
	public NXTConnectionTabelModel(NXTInfo[] nxts) {
		setData(nxts, nxts.length);
	}

	/**
	 * Update the data in the model.
	 *
	 * @param nxts    the NXTInfo array
	 * @param numNXTs the number of NXTs
	 */
	private void setData(NXTInfo[] nxts, int numNXTs) {
		this.numNXTs = numNXTs;
		nxtData = new Object[numNXTs][NUM_COLUMNS];
		// put the object
		for (int i = 0; i < numNXTs; i++) {
			nxtData[i][0] = nxts[i].name;
			nxtData[i][1] = (nxts[i].protocol == NXTCommFactory.USB ? "USB" : "Bluetooth");
			nxtData[i][2] = (nxts[i].deviceAddress == null ? "" : nxts[i].deviceAddress);
			nxtData[i][3] = NXTConnectionState.DISCONNECTED;
		}
	}

	/**
	 * set a row becoming connected
	 * 
	 * @param row
	 * @param state
	 */
	public void setConnected(int row, NXTConnectionState state) {
		nxtData[row][3] = state;
	}

	/**
	 * Return the number of rows
	 * 
	 * @return the number of rows
	 */
	@Override
	public int getRowCount() {
		return numNXTs;
	}

	/**
	 * Return the number of columns
	 * 
	 * @return the number of columns
	 */
	@Override
	public int getColumnCount() {
		return NUM_COLUMNS;
	}

	/**
	 * get the specific value
	 * 
	 * @param row
	 * @param column
	 * @return cell value in (row, col)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		return nxtData[row][column];
	}

	/**
	 * Get the column name
	 * 
	 * @param column the column index
	 * @return the column name
	 */
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	/**
	 * Get the class of the object held in the column cells
	 * 
	 * @param column the column index
	 * @return the class
	 */
	@Override
	public Class<?> getColumnClass(int column) {
		return nxtData[0][column].getClass();
	}
}

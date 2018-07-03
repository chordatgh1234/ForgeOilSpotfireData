import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.ImageIcon;

import java.awt.GridBagConstraints;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.chainsaw.Main;

import java.awt.Panel;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JScrollPane;

public class ExamineWellGui {

	private Controller controller;
	private String uwi; 
	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExamineWellGui window = new ExamineWellGui();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ExamineWellGui() {
		uwi = "???/??-??-???-??W?/?";
		initialize();
		frame.setVisible(true);
	}
	
	public ExamineWellGui(Controller controller, String uwi) {
		this.uwi = uwi;
		this.controller = controller;
		initialize();
		frame.setVisible(true); 
	}
	
	public Object[][] getMnemonics() { 
		String header = "";
		String serviceCo = "";
		int serviceCoIndex = 0;
		for(int i = 0; i < controller.getFormattedDataList().size() ; i++) {
			if (uwi.equals(controller.getFormattedDataList().get(i).getUwi())) { 
				header = controller.getFormattedDataList().get(i).getHeader();
				String[] headerArray = header.split(",");
				for (int j = 0 ; j < headerArray.length ; j++) {
					if (headerArray[j].equals("Service Co.")) {
						serviceCoIndex = j;
						serviceCo = controller.getFormattedDataList().get(i).getRow(0).split(",")[j];
						break;
					}
				}
			}
		}
		if (header == "") {
			return null; 
		}
		
		int headerOffset =  controller.getDataWriter().getHeaderOffset();
		String[] headerArray = header.split(",");
		ArrayList<String> lasMnemonicArray = new ArrayList<String>(); 
		ArrayList<String> mnemonicNameArray = new ArrayList<String>(); 
		
		System.out.println("headerOffset: " + headerOffset + " serviceCoIndex: " + serviceCoIndex );
		int mnemonicIndex = 0;
		for (int i = headerOffset + 1 ; i < serviceCoIndex - 2 ; i++) {
			System.out.println(headerArray[i]);
			if (!headerArray[i].equals("")) {
				mnemonicNameArray.add(controller.getMnemonicList().get(mnemonicIndex).getName());
				lasMnemonicArray.add(headerArray[i]);
			//	System.out.println(controller.getMnemonicList().get(i - headerOffset).getName() + "   " + headerArray[i]);
			}
			mnemonicIndex++;
		}
		if (!headerArray[serviceCoIndex - 2].equals("")) {
			lasMnemonicArray.add(headerArray[serviceCoIndex - 2]); 
			mnemonicNameArray.add("Caliper 2");
		}
		lasMnemonicArray.add(serviceCo);
		mnemonicNameArray.add("Service Co.");
		
		for (int i = serviceCoIndex + 9 ; i < headerArray.length ; i++) {
			mnemonicNameArray.add("UNKNOWN");
			lasMnemonicArray.add(headerArray[i]);
		}
		
		Object[][] objArray = new Object[lasMnemonicArray.size()][2];
		for (int i = 0; i < objArray.length; i++) { 
			objArray[i][0] = mnemonicNameArray.get(i); 
			objArray[i][1] = lasMnemonicArray.get(i);
		}
		return objArray;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 558, 500);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		URL url = Main.class.getResource("/resources/forgeIcon.png");
		ImageIcon forgeIcon = new ImageIcon(url);
		frame.setIconImage(forgeIcon.getImage());
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JLabel lblUwi = new JLabel("UWI: ");
		panel.add(lblUwi);
		
		JLabel lblw = new JLabel(uwi);
		panel.add(lblw);
		
		String [] header = {"Mnemonic Name", "Las Data"};
		Object[][] data = getMnemonics();
		table = new JTable(new MyTableModel(header, data));
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

	}
	
	class MyTableModel extends AbstractTableModel {
		
	    private String[] columnNames;
	    private Object[][] data;
	    
	    public MyTableModel(String[] columnNames, Object[][] data) {
	    	this.columnNames = columnNames; 
	    	this.data = data;
	    }

	    public int getColumnCount() {
	        return columnNames.length;
	    }

	    public int getRowCount() {
	        return data.length;
	    }

	    public String getColumnName(int col) {
	        return columnNames[col];
	    }

	    public Object getValueAt(int row, int col) {
	        return data[row][col];
	    }

	    public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	    }
	}
}

import java.util.ArrayList;

public class WorkingFileData {
	private int typeRow; 
	private String header;
	private ArrayList<String> rows; 
	
	public WorkingFileData() {
		rows = new ArrayList<String>(); 
	}
	
	public int getTypeRow() { return typeRow; } 
	
	public void addHeader(String head) {
		header = head; 
		setTypeRow();
	}
	
	public void setTypeRow() { 
		String[] headerArray = header.split(",");
		for (int i = 0 ; i < headerArray.length ; i++) {
			if ( headerArray[i].equals("Type")) {
				typeRow = i;
			}
		}
	}
    
	public void addRow(String row) {
		rows.add(row); 
	}
	
	public String getRow(int i) {
		return rows.get(i);
	}
	
	public String getHeader() {
		return header;
	}
	
	public int getSize() {
		return rows.size();
	}
	
	public void display() {
		System.out.println(header);
		for(int i = 0; i < rows.size(); i++) {
			System.out.println(rows.get(i));
		}
	}
}

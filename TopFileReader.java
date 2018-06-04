import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TopFileReader {

	private String topFilePath = "C:\\Users\\jhung\\SpotfireDataFiles\\AllGeorgeTopsMikwan.xlsx";
	private ArrayList<TopData> topDataList; 
	private String currentUwi = ""; 
	private ArrayList<String> data; 
	private String township;
	private ArrayList<String> formations;
	
	public TopFileReader() {
		topDataList = new ArrayList<TopData>();
		data = new ArrayList<String> ();
		formations = new ArrayList<String>();
		formations.add("BFS");
		formations.add("JLFU");
		township = "035-22W4";
	}
	
	public TopFileReader(ArrayList<String> forms, String town) {
		formations = forms;
		township = town; 
		topDataList = new ArrayList<TopData>(); 
		data = new ArrayList<String>();
	}
	
	public ArrayList<TopData> readFile() throws IOException { 
		FileInputStream inputStream = new FileInputStream(new File(topFilePath)); 
		Workbook workbook = new XSSFWorkbook(inputStream); 
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator(); 
		iterator.next(); 
		boolean topCheck = false;
		
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			
			int index = 0; 
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				//System.out.println(cell.getStringCellValue().substring(9, 17));
				if (index == 0 && !cell.getStringCellValue().substring(9, 17).equals(township))	{
					index++;
					break;
				}
				
				if (index == 0 && !currentUwi.equals(cell.getStringCellValue())) {
					if (!data.isEmpty()) {
						topDataList.add(new TopData(data));
						topCheck = false;
					}
					data = new ArrayList<String>(); 
					currentUwi = cell.getStringCellValue();
					data.add(currentUwi);
				}
				
				if (index == 2) {
					if (cell.getStringCellValue().substring(1).equals(formations.get(formations.size()-1))) {
						data.add(cell.getStringCellValue());
						cell = cellIterator.next(); 
						data.add(String.valueOf(cell.getNumericCellValue()));
						index++; 
						topCheck = false; 
						break;
					}
					if (cell.getStringCellValue().substring(1).equals(formations.get(0))) {
						topCheck = true; 
						index++;
						break;
					}
					if (topCheck) {
						data.add(cell.getStringCellValue());
					}
				}
				
				if (index == 3) {
					if (topCheck) {
						data.add(String.valueOf(cell.getNumericCellValue()));
						break;
					}
				}
				index++; 
			}
		}
		topDataList.add(new TopData(data));
		workbook.close(); 
		inputStream.close();
		return topDataList;
	}
	
	public static void main(String[] args) { 

		TopFileReader topFileReader = new TopFileReader(); 
		try {
			ArrayList<TopData> topDataList = topFileReader.readFile();
			for (int i = 0; i < topDataList.size(); i++) {
				topDataList.get(i).displayTop();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

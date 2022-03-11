import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dao.CoinsDAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class Main {
    
    public static void main(String[] args) throws IOException {

        CoinsDAO coinsDAO = new CoinsDAO();

        FileInputStream fileInputStream = new FileInputStream("d:\\RC_F01_01_1992_T01_02_2022.xlsx");

        Workbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row = rowIterator.next();
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            String[] tempArr = new String[7];
            java.sql.Date date = null;
            int i = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (i == 1) {
                    date = new java.sql.Date(cell.getDateCellValue().getDate());
                   }
                    tempArr[i] = cell.toString();
                i++;
            }
            Arrays.stream(tempArr).forEach(System.out::println);
            coinsDAO.save(tempArr, date);



            System.out.println();

        }

    }
}

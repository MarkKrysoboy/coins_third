package org.example.models;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadXlsx {
    private MultipartFile file;
    private List<Coin> coins;
    private String str;

    public File multipartFileToFile(MultipartFile multipart, String fileName) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    @Override
    public String toString() {
        return "ReadXlsx{" +
                "file=" + file +
                ", coins=" + coins +
                ", str='" + str + '\'' +
                '}';
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public ReadXlsx() {
    }

    public ReadXlsx(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public void setCoins(List<Coin> coins) {
        this.coins = coins;
    }



    public List<Coin> readXlsx() {
//        file = new File("d:\\RC_2019.xlsx");
        coins = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(multipartFileToFile(file, file.getName()))) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row = rowIterator.next();
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                Coin coin = new Coin();
                if (row.getCell(0) == null) break;

                coin.setPartNumber(row.getCell(0).toString());
                coin.setDt(row.getCell(1).getDateCellValue());
                coin.setCname(row.getCell(2).toString());
                if (row.getCell(3) != null){
                    coin.setSname(row.getCell(3).toString());
                }
                coin.setNominal(row.getCell(4).toString());
                coin.setMetal(row.getCell(5).toString());

                coins.add(coin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coins;

    }


}

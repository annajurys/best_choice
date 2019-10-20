package project.ie;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetExcelFile {

    private File excelFile;
    private FileInputStream fis;
    private XSSFWorkbook workbook;

    public GetExcelFile(String path) {
        try {
            excelFile = new File(path);
            fis = new FileInputStream(excelFile);
            workbook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int howMuchColumns () {
        XSSFSheet sheet = workbook.getSheetAt(0);
        int howMuchColumns = 0;
        int emptyColumns = 0;
        int i = sheet.getRow(0).getLastCellNum()-1;
        while(sheet.getRow(0).getCell(i).toString().isEmpty() && i>=0)
        {
            emptyColumns++;
            i--;
        }
        howMuchColumns=sheet.getRow(0).getLastCellNum()-emptyColumns;
        return howMuchColumns;
    }

    public int howMuchRows () {
        XSSFSheet sheet = workbook.getSheetAt(0);
        int howMuchRows = 0;
        int emptyRows = 0;
        int i = sheet.getLastRowNum()-1;
        while(sheet.getRow(i).getCell(0).toString().isEmpty())
        {
            emptyRows++;
            i--;
        }
        howMuchRows = sheet.getLastRowNum()-emptyRows;
        return howMuchRows;
    }


    public ExcelFile getExcelFileAndSaveInExcelFile(){
        ExcelFile f = new ExcelFile();
        XSSFSheet sheet = workbook.getSheetAt(0);
        int colNumber = howMuchColumns();
        int rowNumber = howMuchRows();
        for (int j = 0 ; j < colNumber ; j++) {
            for (int i = 0 ; i < rowNumber ; i++) {
                if(j==0 && i!=0) {
                    ChoiceNames choices = new ChoiceNames(j,i,sheet.getRow(i).getCell(j).toString());
                    f.addChoices(choices);
                    System.out.println(" choiceName: " + choices.choiceName + " row: " + i + " col:" + j);
                }
            }
            if(j!=0) {
                LinkedList<Object> objectList = new LinkedList<>();
                for(int row=0;row<=rowNumber;row++) {
                    if(row!=0) {
                        if(sheet.getRow(row).getCell(j).toString().isEmpty() || sheet.getRow(row).getCell(j).getCellType()!= CellType.NUMERIC) {
                            objectList.add("null");
                        }
                        else {
                            objectList.add(sheet.getRow(row).getCell(j).toString());
                        }
                    }
                }
                Columns columns = new Columns(j,0,sheet.getRow(0).getCell(j).toString(),objectList);
                f.addColumns(columns);
                System.out.println(" row:" + 0 + " col: " + j + " colName: " + columns.getColName());
                for(int i=0;i<columns.objects.size()-1;i++) {
                    System.out.println(columns.objects.get(i));
                }
            }
        }
        try {
            workbook.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }
}
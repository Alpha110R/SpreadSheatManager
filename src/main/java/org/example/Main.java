package org.example;

import org.example.models.Column;
import org.example.models.ColumnType;
import org.example.models.Sheet;
import org.example.services.SpreadsheetService;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SpreadsheetService service = new SpreadsheetService();
        List<Column> columns = Arrays.asList(
                new Column("A", ColumnType.INT),
                new Column("B", ColumnType.INT),
                new Column("C", ColumnType.INT),
                new Column("D", ColumnType.STRING)
        );

        String sheetId = service.createSheet(columns);
        service.setCell(sheetId, "A", 1, 1);
        service.setCell(sheetId, "A", 2, 2);
        service.setCell(sheetId, "A", 3, "lookup(A,2)");
        service.setCell(sheetId, "A", 4, "sum(A,1,3)");
        /*service.setCell(sheetId, "D", 1, "lookup(C,1)");
        //service.setCell(sheetId, "B", 1, "lookup(A,10)");

        Sheet sheet = service.getSheet(sheetId);
        System.out.println(sheet.getCellEndPoint("A", 1).getValue());
        System.out.println(sheet.getCellEndPoint("B", 1).getValue());
        System.out.println(sheet.getCellEndPoint("C", 1).getValue());
        System.out.println(sheet.getCellEndPoint("D", 1).getValue());

        service.setCell(sheetId, "B", 1, "HELLO");

        System.out.println(sheet.getCellEndPoint("A", 1).getValue());
        System.out.println(sheet.getCellEndPoint("B", 1).getValue());
        System.out.println(sheet.getCellEndPoint("C", 1).getValue());
        System.out.println(sheet.getCellEndPoint("D", 1).getValue());*/


    }
}
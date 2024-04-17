package org.example.services;

import org.example.models.Cell;
import org.example.models.Column;
import org.example.models.ColumnType;
import org.example.models.Sheet;
import org.example.utils.CellVerification;

import java.util.*;

public class SpreadsheetService {
    private Map<String, Sheet> spreadsheets;
    private LookupStrategy lookupAlgorithm;

    public SpreadsheetService() {
        spreadsheets = new HashMap<>();
        lookupAlgorithm = new LookupService();
    }

    public String createSheet(List<Column> columns) {
        Sheet sheet = new Sheet(columns);
        insertNewSheet(sheet);
        return sheet.getId();
    }

    private void insertNewSheet(Sheet sheet){
        spreadsheets.put(sheet.getId(), sheet);
    }

    public Sheet getSheet(String id) {
        return spreadsheets.get(id);
    }

    public synchronized void setCell(String sheetId, String columnName, int rowIndex, Object valueInsertToCell) {
        Sheet sheet = getSheet(sheetId);
        Cell currentCell = new Cell(columnName, rowIndex, valueInsertToCell);
        if (CellVerification.isCellValueALookup(valueInsertToCell)) {
            lookupAlgorithm.handleLookup(sheet, currentCell);
        } else if (CellVerification.isCellValueASum(valueInsertToCell)) {
            System.out.println(LookupService.sumAllCellsInRange(sheet, currentCell));
        } else {
            sheet.setCell(currentCell);
        }
    }


}
/*
1 - one column all the same
2- if there is not value - 0
3- int

- get the columnName, startRowIndex, finishRowIndex
- check column is type valid
- for loop start -> finish include
- get the cell for map
- if
 */

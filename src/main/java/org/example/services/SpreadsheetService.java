package org.example.services;

import org.example.models.Cell;
import org.example.models.Column;
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
        } else {
            sheet.setCell(currentCell);
        }
    }

}

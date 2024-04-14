package org.example.services;

import org.example.models.Cell;
import org.example.models.ColumnType;
import org.example.models.Sheet;
import org.example.utils.CellVerification;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LookupService implements LookupStrategy{
    public LookupService(){}

    public void handleLookup(Sheet sheet, Cell currentCell){
        Set<String> visited = new HashSet<>();
        visited.add(currentCell.getId());
        boolean isCycleNotExist = isThereCycle(sheet, currentCell, visited);
        if(isCycleNotExist)
            sheet.setCell(currentCell);
    }
    private boolean isThereCycle(Sheet sheet, Cell cell, Set<String> visited) {
        updateCellWithLookupToNextCell(cell);
        String targetKey = cell.getColumnToNextCell() + cell.getRowIndexToNextCell();

        if (visited.contains(targetKey)) {
            throw new IllegalArgumentException("Cyclical reference detected involving from " + cell.getId() +" to target cell " + targetKey);
        }

        visited.add(targetKey);
        Cell nextCell = sheet.getCell(cell.getColumnToNextCell(), cell.getRowIndexToNextCell());
        if(Objects.nonNull(nextCell)) {
            if (CellVerification.isCellValueALookup(nextCell.getValue())) {
                isThereCycle(sheet, nextCell, visited);

            } else if (!isColumnsTypeEquals(sheet, cell.getColumnName(), cell.getColumnToNextCell())) {
                throw new IllegalArgumentException("Type mismatch: " + cell.getColumnName() + " with cell " + cell.getId() + " to  " + cell.getColumnToNextCell());
            }
        }
        return true;
    }

    private void updateCellWithLookupToNextCell(Cell cell) {
        try {
            int COLUMN_NAME_FIRST_PARAMETER_LOOKUP = 0;
            int ROW_INDEX_SECOND_PARAMETER_LOOKUP = 1;

            String[] parts = getLookupParams(cell);
            String columnName = parts[COLUMN_NAME_FIRST_PARAMETER_LOOKUP].trim();
            int rowIndex = Integer.parseInt(parts[ROW_INDEX_SECOND_PARAMETER_LOOKUP].trim());

            cell.setColumnToNextCell(columnName).setRowIndexToNextCell(rowIndex);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse lookup function: " + cell.getId());
        }
    }

    private String[] getLookupParams(Cell cell){
        String lookupString = cell.getValue().toString();
        String innerLookup = lookupString.substring("lookup(".length(), lookupString.length() - 1);
        return innerLookup.split(",");

    }

    private boolean isColumnsTypeEquals(Sheet sheet, String sourceColumn, String targetColumn) {
        ColumnType sourceType = sheet.getColumnType(sourceColumn);
        ColumnType targetType = sheet.getColumnType(targetColumn);
        return Objects.equals(sourceType, targetType);
    }

}

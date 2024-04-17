package org.example.models;

import java.util.*;

public class Sheet {
    private String id;
    private Map<String, Column> columnNameToColumn;
    private Map<String, Map<Integer, Cell>> columnNameToMappingRowToCell;

    public Sheet(List<Column> columns) {
        id = UUID.randomUUID().toString();
        columnNameToColumn = new HashMap<>();
        columnNameToMappingRowToCell = new HashMap<>();
        columns.forEach(this::addNewColumn);
    }

    private void addNewColumn(Column newColumn){
        columnNameToColumn.put(newColumn.getName(), newColumn);
        columnNameToMappingRowToCell.put(newColumn.getName(), new HashMap<>());
    }

    public void setCell(Cell cell) throws IllegalArgumentException {
        checkValidationOfCell(cell.getColumnName(), cell.getValue());
        insertCell(cell);
    }

    public void setCellLookUpAfterValidation(Cell cell){
        insertCell(cell);
    }

    private void insertCell(Cell cell){
        columnNameToMappingRowToCell.get(cell.getColumnName()).put(cell.getRowIndex(), cell);
    }

    private void checkValidationOfCell(String cellColumnName, Object cellValue) throws IllegalArgumentException{
        if (!columnNameToColumn.containsKey(cellColumnName)) {
            throw new IllegalArgumentException("Column does not exist");
        }

        Column column = columnNameToColumn.get(cellColumnName);
        if (!isValueValidType(cellValue, column.getType())) {
            throw new IllegalArgumentException("Type mismatch");
        }
    }

    public Cell getCell(String columnName, int rowIndex) {
        return getRowsToCellInColumn(columnName).get(rowIndex);
    }

    public Cell getCellEndPoint(String columnName, int rowIndex) {
        Cell cell = getCell(columnName, rowIndex);

        while (Objects.nonNull(cell) && Objects.nonNull(cell.getColumnToNextCell())){
            cell = getCell(cell.getColumnToNextCell(), cell.getRowIndexToNextCell());
        }

        return cell;
    }

    private boolean isValueValidType(Object value, ColumnType type) {
        switch (type) {
            case STRING:
                return value instanceof String;
            case BOOLEAN:
                return value instanceof Boolean;
            case INT:
                return value instanceof Integer;
            case DOUBLE:
                return value instanceof Double;
            default:
                return false;
        }
    }

    public ColumnType getColumnType(String columnName) {
        if (columnNameToColumn.containsKey(columnName)) {
            return columnNameToColumn.get(columnName).getType();
        } else {
            throw new IllegalArgumentException("Column not found");
        }
    }

    private Map<Integer, Cell> getRowsToCellInColumn(String columnName){
        return columnNameToMappingRowToCell.getOrDefault(columnName, new HashMap<>());
    }

    public String getId() {
        return id;
    }
}

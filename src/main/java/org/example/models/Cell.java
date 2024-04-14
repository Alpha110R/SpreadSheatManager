package org.example.models;

public class Cell {
    private int rowIndexToNextCell;
    private String columnToNextCell;
    private String columnName;
    private int rowIndex;
    private String id;
    private Object value;

    public Cell(String columnName, int rowIndex, Object value){
        setRowIndex(rowIndex);
        setColumnName(columnName);
        createColumnRowIndexId(columnName, rowIndex);
        setValue(value);
    }

    public Cell createColumnRowIndexId(String columnName, int rowIndex){
        setId(columnName + rowIndex);
        return this;
    }

    public int getRowIndexToNextCell() {
        return rowIndexToNextCell;
    }

    public Cell setRowIndexToNextCell(int rowIndexToNextCell) {
        this.rowIndexToNextCell = rowIndexToNextCell;
        return this;
    }

    public String getColumnToNextCell() {
        return columnToNextCell;
    }

    public Cell setColumnToNextCell(String columnToNextCell) {
        this.columnToNextCell = columnToNextCell;
        return this;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public Cell setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public Cell setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
        return this;
    }
}

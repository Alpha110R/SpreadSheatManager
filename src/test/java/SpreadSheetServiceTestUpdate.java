import org.example.models.Cell;
import org.example.models.Column;
import org.example.models.ColumnType;
import org.example.models.Sheet;
import org.example.services.SpreadsheetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpreadSheetServiceTestUpdate {
    private SpreadsheetService service;
    @BeforeEach
    void setUp() {
        service = new SpreadsheetService();
    }

    @Test
    void testCreateSheet() {
        String sheetId = service.createSheet(TestConstants.EMPTY_SHEET);
        Sheet sheet = service.getSheet(sheetId);
        assertNotNull(sheet, "Sheet should not be null");
    }

    @Test
    void testSetCellValidData() {
        String sheetId = service.createSheet(TestConstants.EMPTY_SHEET);
        service.setCell(sheetId, "A", 1, "Hello World");
        service.setCell(sheetId, "E", 1, 1);
        service.setCell(sheetId, "G", 1, true);
        service.setCell(sheetId, "Q", 1, 1.0);
        Cell cellString = service.getSheet(sheetId).getCellEndPoint("A", 1);
        Cell cellInt = service.getSheet(sheetId).getCellEndPoint("E", 1);
        Cell cellBoolean = service.getSheet(sheetId).getCellEndPoint("G", 1);
        Cell cellDouble = service.getSheet(sheetId).getCellEndPoint("Q", 1);
        assertEquals("Hello World", cellString.getValue(), "Cell value should match set value.");
        assertEquals(1, cellInt.getValue(), "Cell value should match set value.");
        assertEquals(true, cellBoolean.getValue(), "Cell value should match set value.");
        assertEquals(1.0, cellDouble.getValue(), "Cell value should match set value.");
    }

    @Test
    void testSetCellNotValidData() {
        String sheetId = service.createSheet(TestConstants.EMPTY_SHEET);
        assertThrows(IllegalArgumentException.class, () -> service.setCell(sheetId, "A", 1, new Object()),
                "Should throw exception for non-existent column.");
    }

    @Test
    void testSetCellNullValue() {
        String sheetId = service.createSheet(TestConstants.EMPTY_SHEET);
        assertThrows(IllegalArgumentException.class, () -> service.setCell(sheetId, "A", 1, null),
                "Should throw exception for non-existent column.");
    }

    @Test
    void testSetCellInvalidColumnType() {
        String sheetId = service.createSheet(TestConstants.EMPTY_SHEET);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.setCell(sheetId, "E", 1, "Not an Integer");
        });
        assertTrue(exception.getMessage().contains("Type mismatch"), "Expected a type mismatch error.");
    }

    @Test
    void testSetCellLookupToDifferentType() {
        String sheetId = service.createSheet(TestConstants.EMPTY_SHEET);
        service.setCell(sheetId, "A", 1, "Hello World");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.setCell(sheetId, "E", 1, "lookup(A,1)");
        });
        assertTrue(exception.getMessage().contains("Type mismatch"), "Expected a type mismatch error.");
    }

    @Test
    void testCyclicalReferenceDetection() {
        String sheetId = service.createSheet(TestConstants.EMPTY_SHEET);
        service.setCell(sheetId, "A", 1, "lookup(D,1)");
        service.setCell(sheetId, "B", 1, "lookup(A,1)");
        service.setCell(sheetId, "C", 1, "lookup(B,1)");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            service.setCell(sheetId, "A", 1, "lookup(C,1)");
        });

        assertTrue(thrown.getMessage().contains("Cyclical reference detected"), "Cyclical reference should be detected.");
    }

    @Test
    void testSetCellToNonExistentColumn() {
        String sheetId = service.createSheet(TestConstants.EMPTY_SHEET);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.setCell(sheetId, "Z", 1, "Data");  // Z column does not exist
        });
        assertTrue(exception.getMessage().contains("Column does not exist"), "Should throw error for non-existent column.");
    }

    @Test
    void testOperationsOnEmptySheet() {
        String sheetId = service.createSheet(Arrays.asList());
        assertThrows(IllegalArgumentException.class, () -> service.setCell(sheetId, "A", 1, "test"),
                "Should throw exception for non-existent column.");
    }

    @Test
    void testBoundaryRowAndColumnIndexes() {
        List<Column> columns = Arrays.asList(new Column("A", ColumnType.STRING));
        String sheetId = service.createSheet(columns);
        assertDoesNotThrow(() -> service.setCell(sheetId, "A", Integer.MAX_VALUE, "Boundary Test"));
        Cell cell = service.getSheet(sheetId).getCellEndPoint("A", Integer.MAX_VALUE);
        assertEquals("Boundary Test", cell.getValue(), "Boundary index should be handled correctly.");
    }

    @Test
    void testFullWorkflowIntegration() {
        List<Column> columns = Arrays.asList(new Column("X", ColumnType.INT), new Column("Y", ColumnType.STRING));
        String sheetId = service.createSheet(columns);
        service.setCell(sheetId, "X", 1, 100);
        service.setCell(sheetId, "Y", 1, "Hello");

        Cell intCell = service.getSheet(sheetId).getCellEndPoint("X", 1);
        Cell stringCell = service.getSheet(sheetId).getCellEndPoint("Y", 1);

        assertEquals(100, intCell.getValue());
        assertEquals("Hello", stringCell.getValue());
    }

    @Test
    void testConcurrentCellUpdates() {
        List<Column> columns = Arrays.asList(new Column("A", ColumnType.STRING));
        String sheetId = service.createSheet(columns);
        Runnable updateTask = () -> {
            for (int i = 0; i < 100; i++) {
                service.setCell(sheetId, "A", 1, "Update " + i);
            }
        };

        Thread thread1 = new Thread(updateTask);
        Thread thread2 = new Thread(updateTask);
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Cell cell = service.getSheet(sheetId).getCellEndPoint("A", 1);
        assertNotNull(cell.getValue(), "Cell should have a value after concurrent updates.");
        assertTrue(cell.getValue().toString().startsWith("Update "), "Cell value should reflect updates.");
    }

    @Test
    void stressTestLargeDataVolume() {
        List<Column> columns = Arrays.asList(new Column("A", ColumnType.INT));
        String sheetId = service.createSheet(columns);
        int numOperations = 10000; // Adjust based on system capabilities and test environment

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numOperations; i++) {
            service.setCell(sheetId, "A", i, i);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Performed " + numOperations + " operations in " + (endTime - startTime) + " ms.");
        assertTrue((endTime - startTime) < 1000, "Operations should be performant under high volume.");
    }


}

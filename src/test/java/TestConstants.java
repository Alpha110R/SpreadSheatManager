import org.example.models.Column;

import java.util.Arrays;
import java.util.List;

import static org.example.models.ColumnType.*;

public class TestConstants {
    public static List<Column> EMPTY_SHEET = Arrays.asList(Column.builder().name("A").type(STRING).build(),
                                                            Column.builder().name("C").type(STRING).build(),
                                                            Column.builder().name("D").type(STRING).build(),
                                                            Column.builder().name("B").type(STRING).build(),
                                                            Column.builder().name("E").type(INT).build(),
                                                            Column.builder().name("G").type(BOOLEAN).build(),
                                                            Column.builder().name("Q").type(DOUBLE).build());
}

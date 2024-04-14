package org.example.utils;

import java.util.Objects;

public class CellVerification {
    public static boolean isCellValueALookup(Object valueInsertToCell){
        return Objects.nonNull(valueInsertToCell) && valueInsertToCell.toString().startsWith("lookup(");
    }
}

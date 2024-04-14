package org.example.services;

import org.example.models.Cell;
import org.example.models.Sheet;

public interface LookupStrategy {
    void handleLookup(Sheet sheet, Cell currentCell);
}

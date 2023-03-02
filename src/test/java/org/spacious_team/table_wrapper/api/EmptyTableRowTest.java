/*
 * Table Wrapper API
 * Copyright (C) 2022  Spacious Team <spacious-team@ya.ru>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spacious_team.table_wrapper.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.*;

class EmptyTableRowTest {

    Table table;
    TableHeaderColumn column;
    EmptyTableRow row;

    void beforeEach() {
        row = new EmptyTableRow(table, 0);
    }

    void getCell(int col) {
        assertNull(row.getCell(col));
    }

    void testGetCell() {
        assertNull(row.getCell(column));
    }

    void getFirstCellNum() {
        assertEquals(-1, row.getFirstCellNum());
    }

    void getLastCellNum() {
        assertEquals(-1, row.getLastCellNum());
    }

    void rowContains(String value) {
        assertFalse(row.rowContains(value));
    }

    void iterator() {
        assertFalse(row.iterator().hasNext());
    }

    void getCellValue() {
        assertNull(row.getCellValue(column));
    }

    void getIntCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getIntCellValue(column));
    }

    void getLongCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getLongCellValue(column));
    }

    void getDoubleCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getDoubleCellValue(column));
    }

    void getBigDecimalCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getBigDecimalCellValue(column));
    }

    void getStringCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getStringCellValue(column));
    }

    void getInstantCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getInstantCellValue(column));
    }

    void getLocalDateTimeCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getLocalDateTimeCellValue(column));
    }

    void testClone() {
        assertEquals(row, row.clone());
    }

    void testGetTable() {
        assertEquals(table, row.getTable());
    }

    void getRowNum(int rowNum) {
        assertEquals(rowNum, new EmptyTableRow(table, rowNum).getRowNum());
    }

    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(EmptyTableRow.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .verify();
    }

    void testToString() {
        assertNotNull(row.toString());
    }
}

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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static nl.jqno.equalsverifier.Warning.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class MutableTableRowTest {

    Table table;
    CellDataAccessObject<?, ReportPageRow> dao;
    TableHeaderColumn headerColumn;
    TableColumn column;
    final int COLUMN_INDEX = 10;
    ReportPageRow wrappedRow;
    MutableTableRow<ReportPageRow> row;

    void setUp() {
        row = spy(new MutableTableRow<>(table, dao));
        row.setRow(wrappedRow);
        lenient().when(headerColumn.getColumn()).thenReturn(column);
        lenient().when(table.getHeaderDescription()).thenReturn(Map.of(column, COLUMN_INDEX));
    }

    void getCell() {
        row.getCell(headerColumn);
        verify(row).getCell(COLUMN_INDEX);
    }

    void testGetCell() {
        row.getCell(COLUMN_INDEX);
        verify(wrappedRow).getCell(COLUMN_INDEX);
    }

    void getRowNum() {
        row.getRowNum();
        verify(wrappedRow).getRowNum();
    }

    void getFirstCellNum() {
        row.getFirstCellNum();
        verify(wrappedRow).getFirstCellNum();
    }

    void getLastCellNum() {
        row.getLastCellNum();
        verify(wrappedRow).getLastCellNum();
    }

    void rowContains() {
        Object object = new Object();
        row.rowContains(object);
        verify(wrappedRow).rowContains(object);
    }

    void iterator() {
        row.iterator();
        verify(wrappedRow).iterator();
    }

    void getCellValue() {
        row.getCellValue(headerColumn);
        verify(dao).getValue(wrappedRow, COLUMN_INDEX);
    }

    void getIntCellValue() {
        row.getIntCellValue(headerColumn);
        verify(dao).getIntValue(wrappedRow, COLUMN_INDEX);
    }

    void getLongCellValue() {
        row.getLongCellValue(headerColumn);
        verify(dao).getLongValue(wrappedRow, COLUMN_INDEX);
    }

    void getDoubleCellValue() {
        row.getDoubleCellValue(headerColumn);
        verify(dao).getDoubleValue(wrappedRow, COLUMN_INDEX);
    }

    void getBigDecimalCellValue() {
        row.getBigDecimalCellValue(headerColumn);
        verify(dao).getBigDecimalValue(wrappedRow, COLUMN_INDEX);
    }

    void getStringCellValue() {
        row.getStringCellValue(headerColumn);
        verify(dao).getStringValue(wrappedRow, COLUMN_INDEX);
    }

    void getInstantCellValue() {
        row.getInstantCellValue(headerColumn);
        verify(dao).getInstantValue(wrappedRow, COLUMN_INDEX);
    }

    void getLocalDateTimeCellValue() {
        row.getLocalDateTimeCellValue(headerColumn);
        verify(dao).getLocalDateTimeValue(wrappedRow, COLUMN_INDEX);
    }

    void testClone() {
        MutableTableRow<ReportPageRow> row = new MutableTableRow<>(table, dao);
        row.setRow(wrappedRow);
        assertEquals(row, row.clone());
    }

    void getTable() {
        assertSame(table, row.getTable());
    }

    void getDao() {
        assertSame(dao, row.getDao());
    }

    void getRow() {
        assertSame(wrappedRow, row.getRow());
    }

    void testEquals() {
        EqualsVerifier
                .forClass(MutableTableRow.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .suppress(NONFINAL_FIELDS)
                .verify();
    }

    void testToString() {
        assertEquals("MutableTableRow(table=table, dao=dao, row=wrappedRow)", row.toString());
    }
}

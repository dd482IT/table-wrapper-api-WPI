// Generated by delombok at Sat Mar 25 11:54:07 EDT 2023
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

import org.checkerframework.checker.nullness.qual.Nullable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Iterator;
import static java.util.Collections.emptyIterator;

class EmptyTableRow implements TableRow {
    private static final String CELL_NOT_FOUND = "Cell not found";
    private final Table table;
    private final int rowNum;

    @Override
    public TableCell getCell(TableHeaderColumn column) {
        return null;
    }

    @Override
    public TableCell getCell(int i) {
        return null;
    }

    @Override
    public int getFirstCellNum() {
        return -1;
    }

    @Override
    public int getLastCellNum() {
        return -1;
    }

    @Override
    public boolean rowContains(Object expected) {
        return false;
    }

    @Override
    public Iterator<TableCell> iterator() {
        return emptyIterator();
    }

    @Override
    public Object getCellValue(TableHeaderColumn column) {
        return null;
    }

    @Override
    public int getIntCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public long getLongCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public double getDoubleCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public BigDecimal getBigDecimalCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public String getStringCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public Instant getInstantCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public LocalDateTime getLocalDateTimeCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public TableRow clone() {
        try {
            return (TableRow) super.clone();
        } catch (final java.lang.Throwable $ex) {
            throw lombok.Lombok.sneakyThrow($ex);
        }
    }

    @java.lang.SuppressWarnings("all")
    public EmptyTableRow(final Table table, final int rowNum) {
        this.table = table;
        this.rowNum = rowNum;
    }

    @java.lang.SuppressWarnings("all")
    public Table getTable() {
        return this.table;
    }

    @java.lang.SuppressWarnings("all")
    public int getRowNum() {
        return this.rowNum;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof EmptyTableRow)) return false;
        final EmptyTableRow other = (EmptyTableRow) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.getRowNum() != other.getRowNum()) return false;
        final java.lang.Object this$table = this.getTable();
        final java.lang.Object other$table = other.getTable();
        if (this$table == null ? other$table != null : !this$table.equals(other$table)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof EmptyTableRow;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getRowNum();
        final java.lang.Object $table = this.getTable();
        result = result * PRIME + ($table == null ? 43 : $table.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "EmptyTableRow(table=" + this.getTable() + ", rowNum=" + this.getRowNum() + ")";
    }
}

// Generated by delombok at Sat Mar 25 11:54:07 EDT 2023
/*
 * Table Wrapper API
 * Copyright (C) 2020  Spacious Team <spacious-team@ya.ru>
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

import java.util.Arrays;
import java.util.Objects;
import static lombok.AccessLevel.PRIVATE;

/**
 * Implements table header kind of
 * <pre>
 * |             One             |             Two            |
 * |   a1    |   a2    |   a3    |   a1   |   a2    |   a3    |
 * | b1 | b2 | b1 | b2 | b1 | b2 |b1 | b2 | b1 | b2 | b1 | b2 |
 * <pre/>
 * Can find index for (Two -> a3 -> b1) column
 */
public class MultiLineTableColumn implements TableColumn {
    private final TableColumn[] rowDescriptors;

    /**
     * @param rowDescriptors each array element describes next rows column
     */
    public static MultiLineTableColumn of(TableColumn... rowDescriptors) {
        return new MultiLineTableColumn(rowDescriptors);
    }

    public static MultiLineTableColumn of(String... rowDescriptors) {
        TableColumn[] descriptors = Arrays.stream(rowDescriptors).map(Objects::requireNonNull).map(PatternTableColumn::of).toArray(TableColumn[]::new);
        return new MultiLineTableColumn(descriptors);
    }

    /**
     * @param headerRows header rows count should be equal to count of row descriptors
     */
    @Override
    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        if (headerRows.length != rowDescriptors.length) {
            throw new TableColumnNotFound("Internal error, " + rowDescriptors.length + " rows expected in table header");
        }
        int columnIndex = firstColumnForSearch;
        int i = 0;
        for (ReportPageRow row : headerRows) {
            TableColumn rowDescriptor = rowDescriptors[i++];
            columnIndex = rowDescriptor.getColumnIndex(columnIndex, row);
        }
        return columnIndex;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "MultiLineTableColumn(rowDescriptors=" + java.util.Arrays.deepToString(this.rowDescriptors) + ")";
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof MultiLineTableColumn)) return false;
        final MultiLineTableColumn other = (MultiLineTableColumn) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (!java.util.Arrays.deepEquals(this.rowDescriptors, other.rowDescriptors)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof MultiLineTableColumn;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + java.util.Arrays.deepHashCode(this.rowDescriptors);
        return result;
    }

    @java.lang.SuppressWarnings("all")
    private MultiLineTableColumn(final TableColumn[] rowDescriptors) {
        this.rowDescriptors = rowDescriptors;
    }
}

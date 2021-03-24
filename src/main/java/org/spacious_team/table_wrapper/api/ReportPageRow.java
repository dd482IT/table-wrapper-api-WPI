/*
 * Table Wrapper API
 * Copyright (C) 2020  Vitalii Ananev <an-vitek@ya.ru>
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

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class ReportPageRow implements Iterable<TableCell> {

    /**
     * @return cell ot null if cell does not exists
     */
    public abstract TableCell getCell(int i);

    public abstract int getRowNum();

    public abstract int getFirstCellNum();

    public abstract int getLastCellNum();

    public abstract boolean rowContains(Object value);

    @RequiredArgsConstructor
    protected static class ReportPageRowIterator<T> implements java.util.Iterator<TableCell> {

        private final java.util.Iterator<T> innerIterator;
        private final Function<T, TableCell> converter;


        @Override
        public boolean hasNext() {
            return innerIterator.hasNext();
        }

        @Override
        public TableCell next() {
            return converter.apply(innerIterator.next());
        }
    }
}
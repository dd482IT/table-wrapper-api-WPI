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

import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
final class TableFactoryHelper {

    static String getTableName(ReportPage reportPage, Predicate<Object> tableNameFinder, TableCellRange range) {
        try {
            if (!Objects.equals(range, TableCellRange.EMPTY_RANGE)) {
                TableCellAddress tableNameCell =
                        reportPage.find(range.getFirstRow(), range.getFirstRow() + 1, tableNameFinder);
                if (!Objects.equals(tableNameCell, TableCellAddress.NOT_FOUND)) {
                    @SuppressWarnings({"nullness", "ConstantConditions"})
                    TableCell cell = requireNonNull(reportPage.getCell(tableNameCell));
                    return cell.getStringValue();
                }
            }
        } catch (Exception ignore) {
            return "<not found>";
        }
        return "<not found>";
    }
}

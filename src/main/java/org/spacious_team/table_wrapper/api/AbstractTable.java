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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

@Slf4j
@EqualsAndHashCode
@ToString(of = {"tableName"})
public abstract class AbstractTable<R extends ReportPageRow> implements Table {

    @Getter
    protected final AbstractReportPage<R> reportPage;
    protected final String tableName;
    @Getter
    private final TableCellRange tableRange;
    @Getter
    private final Map<TableColumn, Integer> headerDescription;
    @Getter
    private final boolean empty;
    /**
     * Offset of first data row. First table row is a header.
     */
    private final int dataRowOffset;

    /**
     * @param tableRange only first and last row numbers matters
     */
    @SuppressWarnings("unused")
    protected <T extends Enum<T> & TableHeaderColumn>
    AbstractTable(AbstractReportPage<R> reportPage,
                  String tableName,
                  TableCellRange tableRange,
                  Class<T> headerDescription,
                  int headersRowCount) {
        this.reportPage = reportPage;
        this.tableName = tableName;
        this.dataRowOffset = 1 + headersRowCount; // table_name + headersRowCount
        this.empty = isEmpty(tableRange, dataRowOffset);
        this.headerDescription = this.empty ?
                Collections.emptyMap() :
                getHeaderDescription(reportPage, tableRange, headerDescription, headersRowCount);
        this.tableRange = empty ?
                tableRange :
                TableCellRange.of(
                        tableRange.getFirstRow(),
                        tableRange.getLastRow(),
                        getColumnIndices(this.headerDescription).min().orElse(tableRange.getFirstColumn()),
                        getColumnIndices(this.headerDescription).max().orElse(tableRange.getLastColumn()));
    }

    @SuppressWarnings("unused")
    protected AbstractTable(AbstractTable<R> table, int appendDataRowsToTop, int appendDataRowsToBottom) {
        this.reportPage = table.reportPage;
        this.tableName = table.tableName;
        this.tableRange = table.tableRange.addRowsToTop(appendDataRowsToTop).addRowsToBottom(appendDataRowsToBottom);
        this.dataRowOffset = table.dataRowOffset;
        this.empty = isEmpty(tableRange, dataRowOffset);
        this.headerDescription = table.headerDescription;
    }

    private static boolean isEmpty(TableCellRange tableRange, int dataRowOffset) {
        return getNumberOfTableRows(tableRange) <= dataRowOffset;
    }

    private static int getNumberOfTableRows(TableCellRange tableRange) {
        return tableRange.getLastRow() - tableRange.getFirstRow() + 1;
    }

    private static <T extends Enum<T> & TableHeaderColumn>
    Map<TableColumn, Integer> getHeaderDescription(AbstractReportPage<?> reportPage, TableCellRange tableRange,
                                                   Class<T> headerDescription,
                                                   int headersRowCount) {
        Map<TableColumn, Integer> columnIndices = new HashMap<>();
        ReportPageRow[] headerRows = new ReportPageRow[headersRowCount];
        for (int i = 0; i < headersRowCount; i++) {
            @Nullable ReportPageRow row = reportPage.getRow(tableRange.getFirstRow() + 1 + i);
            @SuppressWarnings({"nullness"})
            ReportPageRow notNullRow = requireNonNull(row, "Header row is absent");
            headerRows[i] = notNullRow;
        }
        @SuppressWarnings("nullness")
        TableColumn[] columns = Arrays.stream(headerDescription.getEnumConstants())
                .map(TableHeaderColumn::getColumn)
                .toArray(TableColumn[]::new);
        for (TableColumn column : columns) {
            try {
                int columnIndex = column.getColumnIndex(headerRows);
                columnIndices.put(column, columnIndex);
            } catch (OptionalTableColumnNotFound e) {
                log.debug("Optional header column is not found: {}", column, e);
            }
        }
        return unmodifiableMap(columnIndices);
    }

    private static IntStream getColumnIndices(Map<TableColumn, Integer> headerDescription) {
        return headerDescription.values()
                .stream()
                .mapToInt(i -> i);
    }

    public <T> List<T> getData(Object report, Function<TableRow, @Nullable T> rowExtractor) {
        return getDataCollection(report, (row, data) -> {
            @Nullable T result = rowExtractor.apply(row);
            if (result != null) {
                data.add(result);
            }
        });
    }

    public <T> List<T> getDataCollection(Object report, Function<TableRow, @Nullable Collection<T>> rowExtractor) {
        return getDataCollection(report, (row, data) -> {
            @Nullable Collection<T> result = rowExtractor.apply(row);
            if (result != null) {
                data.addAll(result);
            }
        });
    }

    public <T> List<T> getDataCollection(Object report, Function<TableRow, @Nullable Collection<T>> rowExtractor,
                                         BiPredicate<T, T> equalityChecker,
                                         BiFunction<T, T, @Nullable Collection<T>> mergeDuplicates) {
        return getDataCollection(report, (row, data) -> {
            @Nullable Collection<T> result = rowExtractor.apply(row);
            if (result != null) {
                for (T r : result) {
                    addWithEqualityChecker(r, data, equalityChecker, mergeDuplicates);
                }
            }
        });
    }

    private <T> List<T> getDataCollection(Object report, BiConsumer<TableRow, Collection<T>> rowHandler) {
        List<T> data = new ArrayList<>();
        for (@Nullable TableRow row : this) {
            if (row != null) {
                try {
                    rowHandler.accept(row, data);
                } catch (Exception e) {
                    log.warn("Не могу распарсить таблицу '{}' в {}, строка {}",
                            tableName, report, row.getRowNum() + 1, e);
                }
            }
        }
        return data;
    }

    public static <T> void addWithEqualityChecker(T element,
                                                  Collection<T> collection,
                                                  BiPredicate<T, T> equalityChecker,
                                                  BiFunction<T, T, @Nullable Collection<T>> duplicatesMerger) {
        @Nullable T equalsObject = null;
        for (T e : collection) {
            if (equalityChecker.test(e, element)) {
                equalsObject = e;
                break;
            }
        }
        if (equalsObject != null) {
            collection.remove(equalsObject);
            @Nullable Collection<T> mergedCollection = duplicatesMerger.apply(equalsObject, element);
            if (mergedCollection != null) {
                collection.addAll(mergedCollection);
            }
        } else {
            collection.add(element);
        }
    }

    /**
     * {@link TableRow} impl is mutable.
     * For performance issue same object with changed state is provided in each loop cycle.
     * Call {@link TableRow#clone()} if you want to use row object outside stream() block.
     */
    @Override
    public Stream<@Nullable TableRow> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Iterator which returns {@link MutableTableRow} or {@link EmptyTableRow}.
     *
     * @implSpec This iterator never returns null values. Null rows is wrapped by {@link EmptyTableRow}
     * @implNote For performance issue same object with changed state is provided in each loop cycle.
     * Call {@link TableRow#clone()} if you want to use row object outside iterator() block.
     */
    @Override
    public Iterator<TableRow> iterator() {
        return new TableIterator();
    }

    protected class TableIterator implements Iterator<TableRow> {
        private final MutableTableRow<R> tableRow =
                new MutableTableRow<>(AbstractTable.this, getCellDataAccessObject());
        private final int numberOfRows = getNumberOfTableRows(tableRange);
        private int i = dataRowOffset;

        @Override
        public boolean hasNext() {
            return i < numberOfRows;
        }

        /**
         * Returns mutable {@link TableRow} impl. Never returns null value.
         */
        @Override
        public TableRow next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int rowNum = tableRange.getFirstRow() + (i++);
            @Nullable R row = getRow(rowNum);
            if (row == null) {
                return new EmptyTableRow(AbstractTable.this, rowNum);
            }
            tableRow.setRow(row);
            return tableRow;
        }
    }

    @Override
    public @Nullable R getRow(int i) {
        return reportPage.getRow(i);
    }

    @Override
    public @Nullable TableRow findRow(Object value) {
        TableCellAddress address = reportPage.find(value);
        return getMutableTableRow(address);
    }

    @Override
    public @Nullable TableRow findRowByPrefix(String prefix) {
        TableCellAddress address = reportPage.findByPrefix(prefix);
        return getMutableTableRow(address);
    }

    private @Nullable MutableTableRow<R> getMutableTableRow(TableCellAddress address) {
        if (tableRange.contains(address)) {
            MutableTableRow<R> tableRow = new MutableTableRow<>(this, getCellDataAccessObject());
            @SuppressWarnings({"nullness", "ConstantConditions"})
            R row = requireNonNull(getRow(address.getRow()), "Row is empty");
            tableRow.setRow(row);
            return tableRow;
        }
        return null;
    }

    protected abstract CellDataAccessObject<?, R> getCellDataAccessObject();
}
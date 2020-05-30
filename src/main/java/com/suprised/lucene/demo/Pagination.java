/*
 * Created on 2005-3-21
 *
 * Copyright CloverWorxs Inc.
 */
package com.suprised.lucene.demo;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author ltluo
 * 
 */
public class Pagination {
    private Collection row;
    private int rowPerPage;
    private int currentPage;
    private long rawCount;

    /**
     * 
     * @param countRow
     * @param currentPage
     * @param rowPerPage
     */
    public Pagination(long rawCount, int currentPage, int rowPerPage) {
        init(rawCount, currentPage, rowPerPage);
    }

    public Pagination(Collection totalRow) {
        this(totalRow, 1, 10);
    }

    /**
     * 
     * @param totalRow
     * @param currentPage
     * @param rowPerPage
     */
    public Pagination(Collection totalRow, int currentPage, int rowPerPage) {
        totalRow = (totalRow == null ? Collections.EMPTY_LIST : totalRow);
        init(totalRow.size(), currentPage, rowPerPage);

        if (rawCount <= rowPerPage) {
            this.row = totalRow;
        } else {
            this.row = new LinkedList();
            Object[] objArray = totalRow.toArray();
            for (int i = (int) getStartIndex(); i < (int) getEndIndex(); i++) {
                row.add(objArray[i]);
            }
        }
    }

    public Pagination(Collection row, int rawCount) {
        this(row, 1, 10, rawCount);
    }

    public Pagination(Collection row, int currentPage, int rowPerPage,
        long rawCount) {
        this.init(row, currentPage, rowPerPage, rawCount);
    }

    public void setRow(Collection row) {
        this.row = (row == null ? Collections.EMPTY_LIST : row);
    }

    private void init(long rawCount, int currentPage, int rowPerPage) {
        this.currentPage = (currentPage < 1 ? 1 : currentPage);
        this.rawCount = (rawCount < 0 ? 0 : rawCount);
        this.rowPerPage = (rowPerPage < 1 ? (int) this.rawCount : rowPerPage);
        /*if (this.rawCount <= (currentPage - 1) * rowPerPage) {
            this.currentPage = 1;
        }*/
    }

    private void init(Collection row, int currentPage, int rowPerPage,
        long rawCount) {
        init(rawCount, currentPage, rowPerPage);
        this.row = (row == null ? Collections.EMPTY_LIST : row);
    }

    public Collection getRenderList() {
        return getRow();
    }

    public Collection getRow() {
        return this.row;
    }

    public int getPageSize() {
        return getRowPerPage();
    }

    public int getRowPerPage() {
        return this.rowPerPage;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    /**
     * 
     * @return
     */
    public long getRowCount() {
        return this.rawCount;
    }

    public int getNextPage() {
        return isLast() ? currentPage : currentPage + 1;
    }

    public int getPrePage() {
        return isFirst() ? currentPage : currentPage - 1;
    }

    public int getPageCount() {
        int pc = (int) (rawCount / rowPerPage);
        if (rawCount % rowPerPage > 0) {
            pc++;
        }
        return pc;
    }

    public boolean isFirst() {
        if (currentPage == 1) {
            return true;
        }
        return false;
    }

    public boolean isLast() {
        if (currentPage >= getPageCount()) {
            return true;
        }
        return false;
    }

    /**
     * get start index range.
     * 
     * @param endIndex
     * @param rowsPerPage
     * @return
     */
    public long getStartIndex() {
        return (currentPage - 1) * rowPerPage;
    }

    /**
     * get end index range.
     * 
     * @param range
     * @param rowsPerPage
     * @return
     */
    public long getEndIndex() {
        long endIndex = getStartIndex() + rowPerPage;
        if (endIndex > rawCount) {
            endIndex = rawCount;
        }
        return endIndex;
    }

}
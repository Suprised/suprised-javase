package com.suprised.utils.export.build;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.PropertyUtils;

public abstract class FileBuilder implements Serializable{

    private static final long serialVersionUID = -6559858881586037795L;
    
    protected static final String DATE_CELL_STYLE = "yyyy-MM-dd";
    
    protected File file;
    private List<Object> datas;
    private String[] visibleColumns;
    private String[] headers;
    private String header;
    private Locale locale = Locale.getDefault();;
    private String dateFormatString = DATE_CELL_STYLE;

    public FileBuilder() {
    }

    public FileBuilder(List<Object> datas) {
        this.datas = datas;
    }

    public void setVisibleColumns(String[] visibleColumns) {
        this.visibleColumns = visibleColumns;
    }

    public File getFile() {
        try {
            initTempFile();
            resetContent();
            buildFileContent();
            writeToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void initTempFile() throws IOException {
        if (file != null) {
            file.delete();
        }
        file = createTempFile();
    }

    protected void buildFileContent() {
        buildHeader();
        buildColumnHeaders();
        buildRows();
        buildFooter();
    }

    protected void resetContent() {

    }

    protected void buildColumnHeaders() {
        if (visibleColumns.length == 0) return;
        if (headers.length == 0) return ;
        if (visibleColumns.length != headers.length) {
            throw new RuntimeException("表头和要显示的属性大小不一致！！！");
        }
        onHeader();
        for (String header : headers) {
            onNewCell();
            buildColumnHeaderCell(header);
        }
    }

    protected void onHeader() {
        onNewRow();
    }

    protected void buildColumnHeaderCell(String header) {
    }

    protected void buildHeader() {
    }

    private void buildRows() {
        if (datas == null || datas.isEmpty()) {
            return;
        }
        for (Object bean : datas) {
            onNewRow();
            buildRow(bean);
        }
    }

    private void buildRow(Object bean) {
        if (visibleColumns.length == 0) {
            return;
        }
        for (String property : visibleColumns) {
            Object value;
            try {
                value = PropertyUtils.getProperty(bean, property);
            } catch (Exception e){
                e.printStackTrace();
                value = null;
            }
            onNewCell();
            buildCell(value);
        }
    }

    protected void onNewRow() {
    }

    protected void onNewCell() {
    }

    protected abstract void buildCell(Object value);

    protected void buildFooter() {

    }

    protected abstract String getFileExtension();

    protected String getFileName() {
        return "tmp";
    }

    protected File createTempFile() throws IOException {
        File file = new File("d:\\导出的文件.xls");
        if (file.exists()) {
            file.delete() ;
        }
        file.createNewFile();
        return file;
//        return File.createTempFile(getFileName(), getFileExtension());
    }

    protected abstract void writeToFile();

    public void setColumnHeader(String[] headers) {
        this.headers = headers;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    protected int getNumberofColumns() {
        return visibleColumns.length;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    public void setDateFormat(String dateFormat) {
        this.dateFormatString = dateFormat;
    }
    
    protected String getDateFormatString(){
        return dateFormatString;
    }
    
    protected String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString, locale);
        return dateFormat.format(date);
    }
    
}

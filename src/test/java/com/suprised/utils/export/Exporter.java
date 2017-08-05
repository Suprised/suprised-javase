package com.suprised.utils.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import com.suprised.utils.export.build.ExcelFileBuilder;
import com.suprised.utils.export.build.FileBuilder;

public class Exporter {

    protected FileBuilder fileBuilder;
    private String dateFormatString;
    private Locale locale ;
    protected String downloadFileName;
    private ExportType type ;

    public Exporter(List<Object> datas, String[] headers, String[] visibleColumns) {
        // 默认Excel导出
        this(ExportType.EXCEL, datas, headers, visibleColumns) ;
    }
    
    public Exporter(ExportType type, List<Object> datas, String[] headers, String[] visibleColumns) {
        this.type = type;
        setExported(datas);
        setVisibleColumns(visibleColumns);
        setColumnHeader(headers);
    }

    public void setExported(List<Object> datas) {
        fileBuilder = createFileBuilder(datas);
        if (dateFormatString != null) {
            fileBuilder.setDateFormat(dateFormatString);
        }
        if (locale != null) {
            fileBuilder.setLocale(locale);
        }
    }

    public void setVisibleColumns(String[] visibleColumns) {
        fileBuilder.setVisibleColumns(visibleColumns);
    }

    public void setColumnHeader(String[] header) {
        fileBuilder.setColumnHeader(header);
    }

    public void setHeader(String header) {
        fileBuilder.setHeader(header);
    }
    
    public void setDateFormat(String dateFormat){
        this.dateFormatString = dateFormat;
    }
    
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    protected FileBuilder createFileBuilder(List<Object> datas) {
        FileBuilder builder = null;
        switch(this.type) {
            case EXCEL: builder = new ExcelFileBuilder(datas); break;
            case PDF:   builder = null; break;
            case CSV:   builder = null; break;
        }
        if (builder == null)
            throw new UnsupportedOperationException("不支持" + this.type + "导出。");
        return builder;
    }

    public void setDownloadFileName(String fileName){
        downloadFileName = fileName;
    }

    public InputStream getInputStream() {
        try {
            return new FileInputStream(fileBuilder.getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public File getFile() {
        return fileBuilder.getFile() ;
    }
}

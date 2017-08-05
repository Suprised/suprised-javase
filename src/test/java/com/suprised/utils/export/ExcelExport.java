package com.suprised.utils.export;

import java.util.List;

import com.suprised.utils.export.build.ExcelFileBuilder;
import com.suprised.utils.export.build.FileBuilder;

@Deprecated
public class ExcelExport extends Exporter {

    public ExcelExport(List<Object> datas, String[] headers, String[] visibleColumns) {
        super(datas, headers, visibleColumns);
    }

    @Override
    protected FileBuilder createFileBuilder(List<Object> datas) {
        return new ExcelFileBuilder(datas);
    }

}

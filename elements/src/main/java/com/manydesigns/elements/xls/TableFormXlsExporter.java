package com.manydesigns.elements.xls;

import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.forms.TableForm;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.OutputStream;

public class TableFormXlsExporter {
    private final TableForm form;
    private String title;
    private boolean useTemporaryFileDuringWrite = false;

    public TableFormXlsExporter(TableForm form) {
        this.form = form;
    }

    public void export(OutputStream outputStream) throws IOException, WriteException {
        WritableWorkbook workbook;
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setUseTemporaryFileDuringWrite(useTemporaryFileDuringWrite);
        workbook = Workbook.createWorkbook(outputStream, workbookSettings);
        if(StringUtils.isBlank(title)) {
            title = "export";
        }
        WritableSheet sheet =
                workbook.createSheet(title, 0);

        addHeaderToSheet(sheet);

        int i = 1;
        for (TableForm.Row row : form.getRows()) {
            exportRows(sheet, i, row);
            i++;
        }

        workbook.write();
        workbook.close();
        outputStream.flush();
    }

    private void addHeaderToSheet(WritableSheet sheet) throws WriteException {
        WritableCellFormat formatCell = headerExcel();
        int l = 0;
        for (TableForm.Column col : form.getColumns()) {
            sheet.addCell(new jxl.write.Label(l, 0, col.getLabel(), formatCell));
            l++;
        }
    }

    private void exportRows(WritableSheet sheet, int i,
                            TableForm.Row row) throws WriteException {
        int j = 0;
        for (Field field : row) {
            XlsUtil.addFieldToCell(sheet, i, j, field);
            j++;
        }
    }

    private WritableCellFormat headerExcel() {
        WritableFont fontCell = new WritableFont(WritableFont.ARIAL, 12,
             WritableFont.BOLD, true);
        return new WritableCellFormat (fontCell);
    }

    public TableForm getForm() {
        return form;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUseTemporaryFileDuringWrite() {
        return useTemporaryFileDuringWrite;
    }

    public void setUseTemporaryFileDuringWrite(boolean useTemporaryFileDuringWrite) {
        this.useTemporaryFileDuringWrite = useTemporaryFileDuringWrite;
    }
}
package com.manydesigns.elements.xls;

import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.forms.FieldSet;
import com.manydesigns.elements.forms.Form;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.*;

import java.io.IOException;
import java.io.OutputStream;

public class FormXlsExporter {
	private final Form form;
	private boolean useTemporaryFileDuringWrite = false;
	private String title = " ";

	public FormXlsExporter(Form form) {
		this.form = form;
	}

	public void export(OutputStream outputStream) throws IOException, WriteException {
		WritableWorkbook workbook;
		WorkbookSettings workbookSettings = new WorkbookSettings();
		workbookSettings.setUseTemporaryFileDuringWrite(useTemporaryFileDuringWrite);
		workbook = Workbook.createWorkbook(outputStream, workbookSettings);
		WritableSheet sheet = workbook.createSheet(title, workbook.getNumberOfSheets());

		addHeaderToSheet(sheet);

		int i = 1;
		for (FieldSet fieldset : form) {
			int j = 0;
			for (Field field : fieldset.fields()) {
				XlsUtil.addFieldToCell(sheet, i, j, field);
				j++;
			}
			i++;
		}
		workbook.write();
		workbook.close();
		outputStream.flush();
	}

	private WritableCellFormat headerExcel() {
		WritableFont fontCell = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true);
		return new WritableCellFormat(fontCell);
	}

	private void addHeaderToSheet(WritableSheet sheet) throws WriteException {
		WritableCellFormat formatCell = headerExcel();
		int i = 0;
		for (FieldSet fieldset : form) {
			for (Field field : fieldset.fields()) {
				sheet.addCell(new jxl.write.Label(i, 0, field.getLabel(), formatCell));
				i++;
			}
		}
	}

	public Form getForm() {
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
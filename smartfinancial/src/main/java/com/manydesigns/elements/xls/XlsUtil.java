package com.manydesigns.elements.xls;

import com.manydesigns.elements.fields.DateField;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.fields.NumericField;
import com.manydesigns.elements.fields.PasswordField;
import jxl.write.*;
import jxl.write.Number;

import java.math.BigDecimal;
import java.util.Date;

public class XlsUtil {
	public static void addFieldToCell(WritableSheet sheet, int i, int j, Field field) throws WriteException {
		if (field instanceof NumericField) {
			NumericField numField = (NumericField) field;
			if (numField.getValue() != null) {
				jxl.write.Number number;
				BigDecimal decimalValue = numField.getValue();
				if (numField.getDecimalFormat() == null) {
					number = new Number(j, i, decimalValue == null ? null : decimalValue.doubleValue());
				} else {
					NumberFormat numberFormat = new NumberFormat(numField.getDecimalFormat().toPattern());
					WritableCellFormat writeCellNumberFormat = new WritableCellFormat(numberFormat);
					number = new Number(j, i, decimalValue == null ? null : decimalValue.doubleValue(), writeCellNumberFormat);
				}
				sheet.addCell(number);
			}
		} else if (field instanceof PasswordField) {
			Label label = new Label(j, i, PasswordField.PASSWORD_PLACEHOLDER);
			sheet.addCell(label);
		} else if (field instanceof DateField) {
			DateField dateField = (DateField) field;
			DateTime dateCell;
			Date date = dateField.getValue();
			if (date != null) {
				DateFormat dateFormat = new DateFormat(dateField.getDatePattern());
				WritableCellFormat wDateFormat = new WritableCellFormat(dateFormat);
				dateCell = new DateTime(j, i, dateField.getValue() == null ? null : dateField.getValue(), wDateFormat);
				sheet.addCell(dateCell);
			}
		} else {
			Label label = new Label(j, i, field.getStringValue());
			sheet.addCell(label);
		}
	}
}
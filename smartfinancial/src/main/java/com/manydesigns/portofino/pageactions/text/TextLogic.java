package com.manydesigns.portofino.pageactions.text;

import com.manydesigns.portofino.pageactions.text.configuration.Attachment;
import com.manydesigns.portofino.pageactions.text.configuration.TextConfiguration;

public class TextLogic {
	public static Attachment createAttachment(TextConfiguration textConfiguration, String id, String fileName, String contentType, long size) {
		Attachment attachment = new Attachment(id);
		attachment.setFilename(fileName);
		attachment.setContentType(contentType);
		attachment.setSize(size);
		textConfiguration.getAttachments().add(attachment);
		return attachment;
	}

	public static Attachment findAttachmentById(TextConfiguration textConfiguration, String code) {
		for (Attachment current : textConfiguration.getAttachments()) {
			if (current.getId().equals(code)) {
				return current;
			}
		}
		return null;
	}

	public static Attachment deleteAttachmentByCode(TextConfiguration textConfiguration, String code) {
		Attachment attachment = findAttachmentById(textConfiguration, code);
		if (attachment == null) {
			return null;
		} else {
			textConfiguration.getAttachments().remove(attachment);
			return attachment;
		}
	}
}
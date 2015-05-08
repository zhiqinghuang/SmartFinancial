package com.manydesigns.portofino.files;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class TempFileService {
	private static TempFileService IMPL = new SimpleTempFileService();

	public static final Logger logger = LoggerFactory.getLogger(TempFileService.class);

	public abstract TempFile newTempFile(String mimeType, String name) throws IOException;

	public Resolution stream(final TempFile tempFile) throws IOException {
		return new StreamingResolution(tempFile.mimeType, tempFile.getInputStream()) {
			@Override
			protected void stream(HttpServletResponse response) throws Exception {
				super.stream(response);
				tempFile.dispose();
			}
		}.setFilename(tempFile.name);
	}

	public static TempFileService getInstance() {
		return IMPL;
	}

	public static void setInstance(TempFileService tempFileService) {
		logger.info("Using temp file service: {}", tempFileService);
		IMPL = tempFileService;
	}

}
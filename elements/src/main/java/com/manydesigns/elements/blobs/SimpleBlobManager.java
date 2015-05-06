package com.manydesigns.elements.blobs;

import com.manydesigns.elements.util.RandomUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class SimpleBlobManager implements BlobManager {
	public static final Logger logger = LoggerFactory.getLogger(SimpleBlobManager.class);

	protected File blobsDir;

	protected String metaFileNamePattern;

	protected String dataFileNamePattern;

	public SimpleBlobManager(File blobsDir, String metaFileNamePattern, String dataFileNamePattern) {
		this.blobsDir = blobsDir;
		if (!blobsDir.isDirectory() && !blobsDir.mkdirs()) {
			logger.warn("Invalid blobs directory: {}", blobsDir.getAbsolutePath());
		}
		this.metaFileNamePattern = metaFileNamePattern;
		this.dataFileNamePattern = dataFileNamePattern;
	}

	protected File getMetaFile(String code) {
		return RandomUtil.getCodeFile(blobsDir, metaFileNamePattern, code);
	}

	protected File getDataFile(String code) {
		return RandomUtil.getCodeFile(blobsDir, dataFileNamePattern, code);
	}

	public void ensureValidCode(String code) {
		if (!StringUtils.isAlphanumeric(code)) {
			throw new IllegalArgumentException("Code is not alphanumeric: " + code);
		}
	}

	@Override
	public void loadMetadata(Blob blob) throws IOException {
		ensureValidCode(blob.getCode());
		blob.setMetaProperties(loadMetaProperties(getMetaFile(blob.getCode())));
	}

	public Properties loadMetaProperties(File metaFile) throws IOException {
		Properties metaProperties = new Properties();

		InputStream metaStream = null;
		try {
			metaStream = new FileInputStream(metaFile);
			metaProperties.load(metaStream);
		} finally {
			IOUtils.closeQuietly(metaStream);
		}
		return metaProperties;
	}

	@Override
	public InputStream openStream(Blob blob) throws IOException {
		ensureValidCode(blob.getCode());
		blob.setInputStream(new FileInputStream(getDataFile(blob.getCode())));
		return blob.getInputStream();
	}

	@Override
	public void save(Blob blob) throws IOException {
		ensureValidCode(blob.getCode());
		File dataFile = getDataFile(blob.getCode());
		if (!dataFile.getParentFile().isDirectory()) {
			dataFile.getParentFile().mkdirs();
		}
		FileOutputStream out = new FileOutputStream(dataFile);
		try {
			blob.setSize(IOUtils.copyLarge(blob.getInputStream(), out));
		} finally {
			IOUtils.closeQuietly(out);
		}
		File metaFile = getMetaFile(blob.getCode());
		if (!metaFile.getParentFile().isDirectory()) {
			metaFile.getParentFile().mkdirs();
		}
		out = new FileOutputStream(metaFile);
		try {
			blob.getMetaProperties().store(out, "Blob code #" + blob.getCode());
		} finally {
			IOUtils.closeQuietly(out);
		}
		blob.dispose();
	}

	@Override
	public boolean delete(Blob blob) {
		String code = blob.getCode();
		ensureValidCode(code);
		File metaFile = getMetaFile(code);
		File dataFile = getDataFile(code);
		boolean success = true;
		try {
			success = metaFile.delete() && success;
		} catch (Exception e) {
			logger.warn("Cound not delete meta file", e);
			success = false;
		}
		try {
			success = dataFile.delete() && success;
		} catch (Exception e) {
			logger.warn("Cound not delete data file", e);
			success = false;
		}
		return success;
	}

	public File getBlobsDir() {
		return blobsDir;
	}

	public void setBlobsDir(File blobsDir) {
		this.blobsDir = blobsDir;
	}

	public String getMetaFileNamePattern() {
		return metaFileNamePattern;
	}

	public void setMetaFileNamePattern(String metaFileNamePattern) {
		this.metaFileNamePattern = metaFileNamePattern;
	}

	public String getDataFileNamePattern() {
		return dataFileNamePattern;
	}

	public void setDataFileNamePattern(String dataFileNamePattern) {
		this.dataFileNamePattern = dataFileNamePattern;
	}
}
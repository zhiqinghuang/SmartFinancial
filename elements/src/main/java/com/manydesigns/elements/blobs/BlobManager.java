package com.manydesigns.elements.blobs;

import java.io.IOException;
import java.io.InputStream;

public interface BlobManager {

	void loadMetadata(Blob blob) throws IOException;

	InputStream openStream(Blob blob) throws IOException;

	void save(Blob blob) throws IOException;

	boolean delete(Blob blob) throws IOException;

}
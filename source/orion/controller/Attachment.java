package orion.controller;

import java.io.File;

import org.apache.commons.fileupload.FileItem;

public class Attachment {

	protected String name;
	protected String contentType;
	protected Long size;
	protected FileItem fileItem;

	public Attachment(String name, String contentType, Long size, FileItem fileItem) {
		this.name = name;
		this.contentType = contentType;
		this.size = size;
		this.fileItem = fileItem;
	}

	public boolean accept(File file) {
		boolean result = false;
		if (file != null) {
			try {
				fileItem.write(file);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public String getName() {
		return name;
	}

	public String getContentType() {
		return contentType;
	}

	public Long getSize() {
		return size;
	}

}

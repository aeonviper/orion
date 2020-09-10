package orion.view;

import java.util.HashMap;
import java.util.Map;

public class View {

	public enum Type {
		FORWARD, REDIRECT, JSON, JSON_TEXT, TEXT_HTML, TEXT_PLAIN, CUSTOM;
	}

	private Object value;
	private Type type = Type.CUSTOM;
	private Integer statusCode;
	private String contentType;

	public View(String contentType, Integer statusCode, Object value) {
		this.contentType = contentType;
		this.value = value;
		this.statusCode = statusCode;
	}

	public View(String contentType, Object value) {
		this.contentType = contentType;
		this.value = value;
	}

	public View(Type type, Integer statusCode, Object value) {
		this.type = type;
		this.value = value;
		this.statusCode = statusCode;
	}

	public View(Type type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	Map<String, Object> optionMap = new HashMap<>();

	public View setOption(String optionName, Object optionValue) {
		optionMap.put(optionName, optionValue);
		return this;
	}

	public Object getOption(String optionName) {
		if (optionName != null) {
			return optionMap.get(optionName);
		}
		return null;
	}

	public Boolean getOptionAsBoolean(String optionName) {
		if (optionName != null) {
			Object optionValue = optionMap.get(optionName);
			if (optionValue instanceof Boolean) {
				return (Boolean) optionValue;
			}
		}
		return null;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}

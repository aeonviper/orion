package orion.controller;

import java.util.ArrayList;
import java.util.List;

public class Notification {

	private String type = "NOTIFICATION";
	private List<String> noticeList = new ArrayList<>();
	private List<String> errorList = new ArrayList<>();
	private List<String> fieldErrorList = new ArrayList<>();

	public Notification addNotice(String message) {
		noticeList.add(message);
		return this;
	}

	public Notification addError(String message) {
		errorList.add(message);
		return this;
	}

	public Notification addFieldError(String name, String message) {
		fieldErrorList.add(message);
		return this;
	}

	public List<String> getNoticeList() {
		return noticeList;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public List<String> getFieldErrorList() {
		return fieldErrorList;
	}

	public boolean isEmpty() {
		return noticeList.isEmpty() && errorList.isEmpty() && fieldErrorList.isEmpty();
	}

}

package orion.controller;

import java.util.ArrayList;
import java.util.List;

public class Notification {

	private List<String> noticeList = new ArrayList<>();
	private List<String> errorList = new ArrayList<>();
	private List<String> fieldErrorList = new ArrayList<>();

	public void addNotice(String message) {
		noticeList.add(message);
	}

	public void addError(String message) {
		errorList.add(message);
	}

	public void addFieldError(String name, String message) {
		fieldErrorList.add(message);
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

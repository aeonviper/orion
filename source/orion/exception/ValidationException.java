package orion.exception;

import orion.controller.Notification;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 8008351699357781797L;

	protected Notification notification;

	public ValidationException() {
	}

	public ValidationException(Notification notification) {
		this.notification = notification;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

}

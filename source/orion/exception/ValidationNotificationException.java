package orion.exception;

import orion.controller.Notification;

public class ValidationNotificationException extends RuntimeException {

	protected Notification notification;

	public ValidationNotificationException() {
	}

	public ValidationNotificationException(Notification notification) {
		this.notification = notification;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

}

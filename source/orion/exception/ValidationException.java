package orion.exception;

public class ValidationException extends RuntimeException {

	protected String message;

	public ValidationException() {
	}

	public ValidationException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

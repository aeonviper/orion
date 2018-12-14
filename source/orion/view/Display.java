package orion.view;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import orion.core.Utility;

public class Display {

	public static final ZoneOffset zoneOffset = (OffsetDateTime.now(ZoneId.systemDefault())).getOffset();
	public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy h:mm a");

	private enum DisplayType {
		INPUT_TEXT, //
		INPUT_PASSWORD, //
		INPUT_RADIO, //
		INPUT_CHECKBOX, //
		INPUT_SUBMIT, //
		INPUT_BUTTON, //
		INPUT_COLOR, //
		INPUT_DATE, //
		INPUT_DATETIME, //
		INPUT_DATETIME_LOCAL, //
		INPUT_EMAIL, //
		INPUT_WEEK, //
		INPUT_MONTH, //
		INPUT_NUMBER, //
		INPUT_RANGE, //
		INPUT_SEARCH, //
		INPUT_TELEPHONE, //
		INPUT_TIME, //
		INPUT_URL, //
		NONE;
	}

	private DisplayType type;
	private String id;
	private String name;
	private Object value;
	private String cssStyle;
	private String cssClass;
	private String max;
	private String min;
	private String pattern;
	private String maxLength;
	private String size;
	private String placeHolder;
	private boolean enabled = true;

	public Display(DisplayType type) {
		this.type = type;
	}

	public Display id(String id) {
		this.id = id;
		return this;
	}

	public Display cssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
		return this;
	}

	public Display cssClass(String cssClass) {
		this.cssClass = cssClass;
		return this;
	}

	public Display name(String name) {
		this.name = name;
		return this;
	}

	public Display value(Object value) {
		this.value = value;
		return this;
	}

	public Display placeHolder(String placeHolder) {
		this.placeHolder = placeHolder;
		return this;
	}

	public Display max(String max) {
		this.max = max;
		return this;
	}

	public Display max(int max) {
		this.max = new Integer(max).toString();
		return this;
	}

	public Display min(String min) {
		this.min = min;
		return this;
	}

	public Display min(int min) {
		this.min = new Integer(min).toString();
		return this;
	}

	public Display pattern(String pattern) {
		this.pattern = pattern;
		return this;
	}

	public Display maxLength(String maxLength) {
		this.maxLength = maxLength;
		return this;
	}

	public Display maxLength(int maxLength) {
		this.maxLength = new Integer(maxLength).toString();
		return this;
	}

	public Display size(String size) {
		this.size = size;
		return this;
	}

	public Display size(int size) {
		this.size = new Integer(size).toString();
		return this;
	}

	public Display disable() {
		this.enabled = false;
		return this;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");

		if (DisplayType.INPUT_CHECKBOX == type) {
			sb.append("input type=\"checkbox\" name=\"" + name + "\"");
			if (Boolean.TRUE.equals(value)) {
				sb.append(" checked");
			}
		} else if (DisplayType.INPUT_TEXT == type) {
			sb.append("input type=\"text\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		} else if (DisplayType.INPUT_PASSWORD == type) {
			sb.append("input type=\"password\" name=\"" + name + "\"");
		} else if (DisplayType.INPUT_EMAIL == type) {
			sb.append("input type=\"email\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		} else if (DisplayType.INPUT_NUMBER == type) {
			sb.append("input type=\"number\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		} else if (DisplayType.INPUT_RANGE == type) {
			sb.append("input type=\"range\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		} else if (DisplayType.INPUT_DATE == type) {
			sb.append("input type=\"date\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		} else if (DisplayType.INPUT_TIME == type) {
			sb.append("input type=\"time\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		} else if (DisplayType.INPUT_DATETIME == type) {
			sb.append("input type=\"datetime\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		} else if (DisplayType.INPUT_DATETIME_LOCAL == type) {
			sb.append("input type=\"datetime-local\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		} else if (DisplayType.INPUT_MONTH == type) {
			sb.append("input type=\"month\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		} else if (DisplayType.INPUT_WEEK == type) {
			sb.append("input type=\"week\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		} else if (DisplayType.INPUT_TELEPHONE == type) {
			sb.append("input type=\"tel\" name=\"" + name + "\" value=\"" + (value != null ? value : "") + "\"");
		}

		if (id != null) {
			sb.append(" id=\"" + id + "\"");
		}
		if (cssStyle != null) {
			sb.append(" style=\"" + cssStyle + "\"");
		}
		if (cssClass != null) {
			sb.append(" class=\"" + cssClass + "\"");
		}
		if (placeHolder != null) {
			sb.append(" placeHolder=\"" + placeHolder + "\"");
		}
		if (max != null) {
			sb.append(" max=\"" + max + "\"");
		}
		if (min != null) {
			sb.append(" min=\"" + min + "\"");
		}
		if (pattern != null) {
			sb.append(" pattern=\"" + pattern + "\"");
		}
		if (maxLength != null) {
			sb.append(" maxLength=\"" + maxLength + "\"");
		}
		if (size != null) {
			sb.append(" size=\"" + size + "\"");
		}
		if (!enabled) {
			sb.append(" disabled");
		}
		sb.append(">");
		return sb.toString();
	}

	public static Display input(DisplayType type, String name, Object value) {
		Display display = new Display(type);
		display.name(name);
		display.value(value);
		return display;
	}

	public static Display input(DisplayType type, String name) {
		Display display = new Display(type);
		display.name(name);
		return display;
	}

	public static Display inputText(String name, Object value) {
		return input(DisplayType.INPUT_TEXT, name, value);
	}

	public static Display inputPassword(String name) {
		return input(DisplayType.INPUT_PASSWORD, name);
	}

	public static Display inputCheckbox(String name, Boolean value) {
		return input(DisplayType.INPUT_CHECKBOX, name, value);
	}

	public static Display inputEmail(String name, Object value) {
		return input(DisplayType.INPUT_EMAIL, name, value);
	}

	public static Display inputNumber(String name, Object value) {
		return input(DisplayType.INPUT_NUMBER, name, value);
	}

	public static Display inputRange(String name, Object value) {
		return input(DisplayType.INPUT_RANGE, name, value);
	}

	public static Display inputDate(String name, Object value) {
		return input(DisplayType.INPUT_DATE, name, value);
	}

	public static Display inputTime(String name, Object value) {
		return input(DisplayType.INPUT_TIME, name, value);
	}

	public static Display inputDateTime(String name, Object value) {
		return input(DisplayType.INPUT_DATETIME, name, value);
	}

	public static Display inputDateTimeLocal(String name, Object value) {
		return input(DisplayType.INPUT_DATETIME_LOCAL, name, value);
	}

	public static Display inputMonth(String name, Object value) {
		return input(DisplayType.INPUT_MONTH, name, value);
	}

	public static Display inputWeek(String name, Object value) {
		return input(DisplayType.INPUT_WEEK, name, value);
	}

	public static Display inputTelephone(String name, Object value) {
		return input(DisplayType.INPUT_TELEPHONE, name, value);
	}

	public static String selection(String name, List list, String keyQualifier, String valueQualifier, String initialKey, String initialValue, Object currentKey) {
		StringBuilder sb = new StringBuilder();
		sb.append("<select name='" + name + "'>");
		if (initialKey != null && initialValue != null) {
			sb.append("<option value='" + initialKey + "'>").append(initialValue).append("</option>");
		}
		for (Object entry : list) {
			String selected = "";
			if (keyQualifier != null && valueQualifier != null) {
				try {
					String key = BeanUtils.getProperty(entry, keyQualifier);
					String value = BeanUtils.getProperty(entry, valueQualifier);
					if (currentKey != null && currentKey.toString().equals(key)) {
						selected = " selected";
					}
					sb.append("<option value='" + key + "'").append(selected).append(">").append(value).append("</option>");
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				}
			} else {
				if (currentKey != null && currentKey.equals(entry)) {
					selected = " selected";
				}
				sb.append("<option value='" + entry + "'").append(selected).append(">").append(entry).append("</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}

	public static String selection(String name, List list, String keyQualifier, String valueQualifier, Object currentKey) {
		return selection(name, list, keyQualifier, valueQualifier, null, null, currentKey);
	}

	public static String property(Object value) {
		if (value != null) {
			return value.toString();
		}
		return "";
	}

	public static String propertyDateTime(Object value) {
		if (value != null && value instanceof Long) {
			return dateTimeFormatter.format(LocalDateTime.ofEpochSecond((Long) value, 0, zoneOffset));
		}
		return "";
	}

	public static String propertyCurrency(Object value) {
		if (value != null && value instanceof Long) {
			return Utility.numberFormatCurrency.format((Long) value);
		}
		return "";
	}

}

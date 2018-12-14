package orion.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class Utility {

	public static final Gson gson = new Gson();
	public static final NumberFormat numberFormatCurrency;

	static {
		DecimalFormat decimalFormat = new DecimalFormat();
		DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
		decimalFormatSymbols.setGroupingSeparator('.');
		decimalFormatSymbols.setDecimalSeparator(',');
		decimalFormatSymbols.setCurrencySymbol("$ ");
		decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
		numberFormatCurrency = decimalFormat;
	}
	
	public static List<String> readFileResource(String filename) {
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		String line;
		URL url;
		List<String> lines = new ArrayList<String>();

		try {
			url = Thread.currentThread().getContextClassLoader().getResource(filename);
			if (url != null) {
				inputStreamReader = new InputStreamReader(url.openStream());
				reader = new BufferedReader(inputStreamReader);
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading file. Cause: " + e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException eio) {
				eio.printStackTrace();
			}

			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (IOException eio) {
				eio.printStackTrace();
			}
		}

		return lines;
	}

	public static <T> T[] parseJsonArray(Class<T> type, String data) {
		JsonParser parser = new JsonParser();
		JsonArray jsonArray = parser.parse(data).getAsJsonArray();
		T[] array = (T[]) Array.newInstance(type, jsonArray.size());
		for (int i = 0; i < jsonArray.size(); i++) {
			array[i] = gson.fromJson(jsonArray.get(i), type);
		}
		return array;
	}
	
	public static String getPath(HttpServletRequest request) {
		return request.getRequestURI().substring(request.getContextPath().length());
	}
	
	public static String stripText(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		return str.trim();
	}
	
	public static void slurp(List<String> logList, Exception e) {
		for (StackTraceElement element : e.getStackTrace()) {
			logList.add(element.toString());
		}
	}

}

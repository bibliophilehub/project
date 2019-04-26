package com.inext.utils.helipay;

public abstract class Objutil {

	public static <T> T notBlank(T t, String message, int max) {
		if (t == null)
			throw new IllegalArgumentException(message);
		int length = t.toString().trim().length();
		if (length == 0)
			throw new IllegalArgumentException(message);
		if (max > 0 && length > max)
			throw new IllegalArgumentException(message);
		return t;
	}
}

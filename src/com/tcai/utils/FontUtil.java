package com.tcai.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtil {
	static String typefaceName = "HKSVWZ.ttf";
	static Typeface tf;

	public static Typeface getCustomFont(Context context) {
		if (tf == null)
			tf = Typeface.createFromAsset(context.getAssets(),
					String.format("font/%s", typefaceName));
		return tf;
	}

	public static String getTypefaceName() {
		return typefaceName;
	}

	public static void setTypefaceName(String typefaceName) {
		FontUtil.typefaceName = typefaceName;
	}

}

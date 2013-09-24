package com.dolph.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.SparseArray;

public class Utils {

	private final static String TAG = "Utils";
	private Context context;

	public Utils(Context context) {
		this.context = context;
	}

	public static String durationFormat(int duration) {
		StringBuffer sb = new StringBuffer();
		if (duration > 3600) {
			if (duration < 36000) {
				sb.append("0" + duration / 3600 + ":");
			} else {
				sb.append(duration / 3600 + ":");
			}
		}
		if ((duration % 3600) < 600) {
			sb.append("0" + (duration % 3600) / 60 + ":");
		} else {
			sb.append((duration % 3600) / 60 + ":");
		}
		if (duration % 60 < 10) {
			sb.append("0" + duration % 60);
		} else {
			sb.append(duration % 60);
		}
		return sb.toString();
	}

	public boolean checkNetworkAvailable() {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}

	}

}

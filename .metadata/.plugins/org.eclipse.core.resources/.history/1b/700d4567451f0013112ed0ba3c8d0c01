package com.dolph.twilioapp;

import android.content.Context;

public class AppValues {
	private Context mContext;
	private AppSettings setting;

	public AppValues(Context context) {
		this.mContext = context;
		setting = new AppSettings(context);
	}

	private static int currentUserId;
	private static String currentUserName;
	private static String currentPhoneNumber;
	private static String currentPassword;
	private static boolean logined = false;

	public String getCurrentPassword() {
		if (currentPassword == null || "".equals(currentPassword)) {
			currentPassword = setting.getSettingString("password", "");
		}
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		setting.setSettingsString("password", currentPassword);
		AppValues.currentPassword = currentPassword;
	}

	public String getCurrentUserName() {
		if (currentUserName == null || "".equals(currentUserName)) {
			currentUserName = setting.getSettingString("username", "");
		}
		return currentUserName;
	}

	public void setCurrentUserName(String currentUserName) {
		setting.setSettingsString("username", currentUserName);
		AppValues.currentUserName = currentUserName;
	}

	public String getCurrentPhoneNumber() {
		if (currentPhoneNumber == null || "".equals(currentPhoneNumber)) {
			currentPhoneNumber = setting.getSettingString("phonenumber", "");
		}
		return currentUserName;
	}

	public void setCurrentPhoneNumber(String currentPhoneNumber) {
		setting.setSettingsString("phonenumber", currentPhoneNumber);
		AppValues.currentPhoneNumber = currentPhoneNumber;
	}

	public int getCurrentUserId() {
		if (currentUserId == 0)
			currentUserId = setting.getSettingInt("user_id", 0);
		return currentUserId;
	}

	public void setCurrentUserId(int currentUserId) {
		setting.setSettingsInt("user_id", currentUserId);
		AppValues.currentUserId = currentUserId;
	}

	public boolean isLogined() {
		logined = setting.getSettingBoolean("logined", false);
		return logined;
	}

	public void setLogined(boolean logined) {
		setting.setSettingsBoolean("logined", logined);
	}

}
